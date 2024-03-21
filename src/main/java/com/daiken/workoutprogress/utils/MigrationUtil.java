package com.daiken.workoutprogress.utils;

import com.daiken.workoutprogress.annotation.Migratable;
import com.daiken.workoutprogress.annotation.Migration;
import lombok.Getter;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;

/**
 * Helper class that contains a migration, the class it's contained in and references to its respective annotations.
 */
@Getter
public class MigrationUtil {

    public Object migratable;
    public Method migration;

    public Migratable migratableAnnotation;
    public Migration migrationAnnotation;

    public MigrationUtil(Object migratable, Method migration) {
        this.migratable = migratable;
        this.migration = migration;
        this.migratableAnnotation = AnnotationUtils.getAnnotation(migratable.getClass(), Migratable.class);
        this.migrationAnnotation = AnnotationUtils.getAnnotation(migration, Migration.class);
    }

    /**
     * Factory method for constructing new `MigrationUtil` instances.
     *
     * @param migratable The migration class instance.
     * @param migration  The migration method.
     * @return An instance of `MigrationUtil`.
     */
    public static MigrationUtil of(Object migratable, Method migration) {
        return new MigrationUtil(migratable, migration);
    }

    public int order() {
        if (migrationAnnotation != null) {
            return migrationAnnotation.order();
        }

        return -1;
    }

    public int duration() {
        if (migrationAnnotation != null) {
            return migrationAnnotation.duration();
        }

        return Migration.DEFAULT_DURATION;
    }

}
