package com.daiken.workoutprogress.api.graphql.input;

import java.util.List;

/**
 * Input for a program log.
 *
 * @param programLogGroupId The program log group id.
 * @param exerciseId        The exercise id.
 * @param repetitions       The repetitions.
 * @param logValue          The log value.
 * @param subdivisions      The subdivisions.
 * @param intervalSeconds   The interval seconds.
 * @param cooldownSeconds   The cooldown seconds.
 * @param effort            The effort.
 */
public record ProgramLogInput(String programLogGroupId, String exerciseId, int repetitions, LogValueInput logValue,
                              List<ProgramLogInput> subdivisions, int intervalSeconds, int cooldownSeconds,
                              int effort) {
}
