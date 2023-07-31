package com.daiken.workoutprogress.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Migration {

    /**
     * Unique key for this migration. Defaults to an empty String, which will be replaced by the migration method name
     * when determining the `migrationKey` when storing/retrieving from the database.
     *
     * @return Unique key for this migration.
     */
    String key() default "";

    /**
     * Name of the author of this migration.
     *
     * @return Name of the migration author.
     */
    String author();

    enum Type {
        ORDERED,
        UNORDERED
    }

    /**
     * Type of this migration. Ordered migrations are required to have a unique order key that indicates the order in
     * which they need to be executed, globally. Unordered migrations can be run whenever.
     *
     * @return The type of this migration.
     */
    Type type() default Type.ORDERED;

    /**
     * Order of this migration if its type is ORDERED.
     *
     * @return The order of this migration.
     */
    int order() default -1;

    int DEFAULT_DURATION = 60 * 1000;

    /**
     * Indicator for the duration length of this migration.
     *
     * @return The number of milliseconds this migration is allowed to take.
     */
    int duration() default DEFAULT_DURATION;

}
