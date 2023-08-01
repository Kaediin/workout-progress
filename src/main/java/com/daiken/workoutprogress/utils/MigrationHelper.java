package com.daiken.workoutprogress.utils;

import com.daiken.workoutprogress.annotation.Migration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * Helper class that contains static methods to help dealing with migrations.
 */
public class MigrationHelper {

    /**
     * Check if a method has the `Migration` annotation.
     *
     * @param method The method to check.
     * @return Whether the method has the right annotation.
     */
    public static boolean hasMigrationAnnotation(Method method) {
        return method.isAnnotationPresent(Migration.class);
    }

    /**
     * Get the `Migration` annotation instance from a method.
     *
     * @param method The method to get the annotation for.
     * @return The annotation instance (if it exists).
     */
    public static Migration getMigrationAnnotation(Method method) {
        return AnnotationUtils.getAnnotation(method, Migration.class);
    }

    /**
     * Check if a method has the `Migration` annotation and is of type `Migration.Type.ORDERED`.
     *
     * @param method The method to check.
     * @return Whether the method is an ordered migration.
     */
    public static boolean isOrderedMigration(Method method) {
        return Optional.of(getMigrationAnnotation(method))
                .map(annotation -> annotation.type() == Migration.Type.ORDERED)
                .orElse(false);
    }

    /**
     * Check if a method has the `Migration` annotation and is of type `Migration.Type.UNORDERED`.
     *
     * @param method The method to check.
     * @return Whether the method is an unordered migration.
     */
    public static boolean isUnorderedMigration(Method method) {
        return Optional.of(getMigrationAnnotation(method))
                .map(annotation -> annotation.type() == Migration.Type.UNORDERED)
                .orElse(false);
    }

    /**
     * Return the migration key for a given migration method.
     *
     * @param method The method to get the key for.
     * @return The migration key for the method.
     */
    public static String getMigrationKey(Method method) {
        return Optional.of(method)
                .map(MigrationHelper::getMigrationAnnotation)
                .filter(annotation -> StringUtils.hasLength(annotation.key()))
                .map(Migration::key)
                .orElseGet(method::getName);
    }

}
