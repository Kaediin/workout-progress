package com.daiken.workoutprogress.api.graphql.input;

/**
 * Input for scheduling a program.
 *
 * @param programId             ID of the program to schedule
 * @param workoutName           Name of the workout
 * @param scheduleZonedDateTime ZonedDateTime of the schedule date
 * @param zonedDateTime         ZonedDateTime of the creation
 * @param remark                Remark for the schedule
 */
public record ScheduledProgramInput(String programId, String workoutName, String scheduleZonedDateTime,
                                    String zonedDateTime,
                                    String remark) {
}
