package com.daiken.workoutprogress.api.graphql.input;

/**
 * Represents the input for creating an exercise log.
 *
 * @param zonedDateTimeString The date and time of the exercise log
 * @param exerciseId          The ID of the exercise associated with the log
 * @param repetitions         The number of repetitions performed
 * @param logValue            The value of the log
 * @param warmup              Whether the log is a warmup
 * @param remark              Any remarks about the log
 * @param effort              The effort of the log
 */
public record ExerciseLogInput(String zonedDateTimeString, String exerciseId, long repetitions, LogValueInput logValue,
                               Boolean warmup, String remark, Integer effort) {

}
