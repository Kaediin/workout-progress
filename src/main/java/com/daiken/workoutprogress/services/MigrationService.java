package com.daiken.workoutprogress.services;

import com.daiken.workoutprogress.annotation.Migratable;
import com.daiken.workoutprogress.annotation.Migration;
import com.daiken.workoutprogress.model.MigrationRecord;
import com.daiken.workoutprogress.utils.MigrationHelper;
import com.daiken.workoutprogress.utils.MigrationUtil;
import io.sentry.Sentry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MigrationService {

    private static final Logger logger = LoggerFactory.getLogger(MigrationService.class);
    private static final String LOCK_KEY = "Migrations";

    private ApplicationContext context;

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * Run `Migration.Type.ORDERED` migrations in-order at startup. Migrations that already have run are skipped.
     * Before running migrations, certain validation is done.
     *
     * @param event The `ApplicationReadyEvent` for us to get the `ApplicationContext` from.
     */
    @EventListener
    public void start(ApplicationReadyEvent event) {
        context = event.getApplicationContext();

        logger.info("start: running ordered migrations...");

        List<MigrationUtil> migrations;

        // Assume our setup, validation and other functions take 5 minutes
        int duration = 5 * 60 * 1000;

        try {
            // Discover all ordered migrations
            migrations = findOrderedMigrations();

            // Add the duration of each migration to run to the total duration
            duration = duration + migrations.stream()
                    .filter(pair -> !hasMigrationRun(pair))
                    .map(MigrationUtil::duration)
                    .reduce(0, Integer::sum);
        } catch (Exception e) {
            logger.error("start: failure while computing duration of migrations to run", e);
            Sentry.captureException(e);
            return;
        }

        List<String> errors;

        try {
            // Validate all ordered migrations
            errors = validateMigrations(migrations);

            if (!errors.isEmpty()) {
                throw new Exception(String.join(", ", errors));
            }
        } catch (Exception e) {
            logger.error("start: failure validating migrations: " + e.getMessage(), e);
            Sentry.captureException(e);
            return;
        }

        List<String> messageList = new ArrayList<>();

        try {
            for (MigrationUtil pair : migrations) {
                // Run every migration that hasn't run yet
                if (!hasMigrationRun(pair)) {
                    executeMigration(pair, messageList);
                    saveMigration(pair);
                }
            }
        } catch (Exception e) {
            logger.error("start: failure executing migration: " + e.getMessage() + "\nmessages:\n" +
                    String.join("\n", messageList), e);
            Sentry.captureException(e);
        }

        logger.info("start: finished running ordered migrations");
    }

    /**
     * Run a particular `Migration.Type.UNORDERED` migration.
     *
     * @param key The unique key of the migration you want to run.
     * @return An error string; `null` if no errors occurred.
     */
    public String runUnordered(String key) {
        logger.info("runUnordered: running unordered migration '" + key + "'...");

        List<MigrationUtil> migrations = null;

        // Assume our setup, validation and other functions take 5 minutes
        int duration = 5 * 60 * 1000;

        try {
            // Discover all matching unordered migrations
            migrations = findUnorderedMigrationsByKey(key);

            // Add the duration of each matching unordered migration to run to the total duration
            duration = duration + migrations.stream()
                    .map(MigrationUtil::duration)
                    .reduce(0, Integer::sum);
        } catch (Exception e) {
            logger.error("runUnordered: failure while computing duration of migration '" + key + "': " +
                    e.getMessage(), e);
            Sentry.captureException(e);
            return "failure while computing duration of migration '" + key + "': " + e.getMessage();
        }

        List<String> errors;

        try {
            // Validate all matching unordered migrations
            errors = validateMigrations(migrations);

            if (!errors.isEmpty()) {
                throw new Exception(String.join(", ", errors));
            }
        } catch (Exception e) {

            logger.error("runUnordered: failure validating unordered migration '" + key + "': " + e.getMessage(), e);
            Sentry.captureException(e);
            return "failure validating unordered migration '" + key + "': " + e.getMessage();
        }

        if (migrations.isEmpty()) {

            logger.error("runUnordered: unordered migration '" + key + "' does not exist");

            return "unordered migration '" + key + "' does not exist";
        } else if (migrations.size() > 1) {

            logger.error("runUnordered: unordered migration '" + key + "' is not unique");

            return "unordered migration '" + key + "' is not unique";
        }

        List<String> messageList = new ArrayList<String>();

        try {
            for (MigrationUtil pair : migrations) {
                executeMigration(pair, messageList);
                saveMigration(pair);
            }
        } catch (Exception e) {

            logger.error("runUnordered: failure executing unordered migration '" + key + "': " + e.getMessage(), e);

            for (String message : messageList) {
                logger.error(message);
            }
            Sentry.captureException(e);
            return "failure executing unordered migration '" + key + "': " + e.getMessage() + "\nmessages:\n" +
                    String.join("\n", messageList);
        }


        logger.info("runUnordered: finished running unordered migrations\nmessages:\n" +
                String.join("\n", messageList));

        return "[OK]\n" + String.join("\n", messageList);
    }

    /**
     * Validate a list of migrations.
     *
     * @param migrations The `MigrationUtil` helper instances to validate.
     * @return A list of error strings; empty if no errors found.
     */
    public List<String> validateMigrations(List<MigrationUtil> migrations) {
        // Validate each migration method
        List<String> errors = migrations.stream()
                .map(MigrationUtil::getMigration)
                .map(this::validateMigration)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // Get all ordered migrations
        List<Migration> orderedAnnotations = migrations.stream()
                .map(MigrationUtil::getMigrationAnnotation)
                .filter(Objects::nonNull)
                .filter(annotation -> annotation.type() == Migration.Type.ORDERED)
                .toList();

        // Get the unique number of order values from all ordered migrations
        long distinctOrderCount = orderedAnnotations.stream()
                .map(Migration::order)
                .distinct()
                .count();

        // Check if the total number of ordered migrations exceeds the unique number of order values
        if (distinctOrderCount < orderedAnnotations.size()) {
            errors.add("several ORDERED migrations have the same order");
        }

        // Get the unique number of keys from all migrations
        long distinctKeyCount = migrations.stream()
                .map(MigrationUtil::getMigration)
                .map(MigrationHelper::getMigrationKey)
                .distinct()
                .count();

        // Check if the total number of keys exceeds the unique number of keys
        if (distinctKeyCount != migrations.size()) {
            errors.add("several migrations have the same key");
        }

        return errors;
    }

    /**
     * Validate an individual migration method. The return type must be `void`, the method must have the correct
     * `Migration` annotation and if its type is `Migration.Type.ORDERED`, its order value must be 0 or higher.
     *
     * @param method The method to validate.
     * @return An error string if the method does not validate; `null` if it validates.
     */
    public String validateMigration(Method method) {
        if (!Void.TYPE.equals(method.getReturnType())) {
            return method.getName() + " does not return void";
        }

        Optional<Class<?>> firstParameterType = Arrays.stream(method.getParameterTypes())
                .findFirst();

        if (firstParameterType.isEmpty() ||
                !List.class.isAssignableFrom(firstParameterType.get())) {
            return method.getName() + " requires a first parameter of type `List<String>`";
        }

        Migration annotation = MigrationHelper.getMigrationAnnotation(method);

        if (annotation == null) {
            return method.getName() + " does not have the Migration annotation";
        }

        if (annotation.type() == Migration.Type.ORDERED && annotation.order() < 0) {
            return method.getName() + " is ORDERED but does not have a correct order: " + annotation.order() + " < 0";
        }

        return null;
    }

    /**
     * Find and return all ordered migrations within the application by searching for all beans that have the
     * `Migratable` annotation, and taking all methods from those classes that have the `Migration` annotation. Then,
     * filter for ordered migrations, and sort the result by their order value.
     *
     * @return An ordered list of migrations to run.
     */
    private List<MigrationUtil> findOrderedMigrations() {
        return context.getBeansWithAnnotation(Migratable.class)
                .values()
                .stream()
                .flatMap(migratable -> Arrays.stream(migratable.getClass().getMethods())
                        .filter(MigrationHelper::hasMigrationAnnotation)
                        .filter(MigrationHelper::isOrderedMigration)
                        .map(method -> MigrationUtil.of(migratable, method)))
                .sorted(Comparator.comparingInt(MigrationUtil::order))
                .collect(Collectors.toList());
    }

    /**
     * Find and return all unordered migrations that match the given `key`.
     *
     * @param key The key to search for.
     * @return A list of unordered migrations that match the key.
     */
    private List<MigrationUtil> findUnorderedMigrationsByKey(String key) {
        return context.getBeansWithAnnotation(Migratable.class)
                .values()
                .stream()
                .flatMap(migratable -> Arrays.stream(migratable.getClass().getMethods())
                        .filter(MigrationHelper::hasMigrationAnnotation)
                        .filter(MigrationHelper::isUnorderedMigration)
                        .filter(method -> key.equals(MigrationHelper.getMigrationKey(method)))
                        .map(method -> MigrationUtil.of(migratable, method)))
                .collect(Collectors.toList());
    }

    /**
     * Check if a given migration has already run.
     *
     * @param pair The migration to check.
     * @return Whether the migration has run or not.
     */
    public boolean hasMigrationRun(MigrationUtil pair) {
        String migrationKey = MigrationHelper.getMigrationKey(pair.migration);

        return mongoTemplate.exists(Query.query(Criteria.where("key").is(migrationKey)), "migrationRecord");
    }

    /**
     * Run an individual migration by resolving all parameter bean references, then invoking the migration method.
     *
     * @param pair The migration to run.
     */
    private void executeMigration(MigrationUtil pair, List<String> messageList) {
        Stream<Object> parameterBeans = Arrays.stream(pair.migration.getParameterTypes())
                .skip(1)
                .map(context::getBean);
        Object[] parameters = Stream.concat(Stream.of(messageList), parameterBeans).toArray();

        ReflectionUtils.invokeMethod(pair.migration, pair.migratable, parameters);
    }

    /**
     * Record the running of a migration in our database.
     *
     * @param pair The migration to record.
     */
    private void saveMigration(MigrationUtil pair) {
        String migrationKey = MigrationHelper.getMigrationKey(pair.migration);
        Integer migrationOrder = pair.order();

        mongoTemplate.save(new MigrationRecord(migrationKey, migrationOrder), "migrationRecord");
    }

}
