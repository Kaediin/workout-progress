package com.daiken.workoutprogress.api.graphql.input;

/**
 * Input for creating a new program.
 *
 * @param name          The name of the program.
 * @param zonedDateTime The date and time when the program was created.
 * @param remark        A remark for the program.
 */
public record ProgramInput(String name, String zonedDateTime, String remark) {
}
