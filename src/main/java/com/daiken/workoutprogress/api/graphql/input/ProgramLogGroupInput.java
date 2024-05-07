package com.daiken.workoutprogress.api.graphql.input;

import com.daiken.workoutprogress.models.ProgramLogGroupType;

import java.util.List;

/**
 * Input for a group of program logs.
 *
 * @param programId The id of the program.
 * @param type      The type of the program log group.
 * @param logs      The logs in the group.
 */
public record ProgramLogGroupInput(String programId, ProgramLogGroupType type, List<ProgramLogInput> logs) {
}
