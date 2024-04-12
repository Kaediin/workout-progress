package com.daiken.workoutprogress.api.graphql.input;

import com.daiken.workoutprogress.models.MuscleGroup;

import java.util.List;

/**
 * Represents the input for creating a workout.
 *
 * @param name          The name of the workout
 * @param muscleGroups  The muscle groups worked by the workout
 * @param zonedDateTime The date and time of the workout
 * @param remark        Any remark about the workout
 */
public record WorkoutInput(String name, List<MuscleGroup> muscleGroups, String zonedDateTime, String remark) {
    @Override
    public String toString() {
        // return all the fields in a string with null safety
        return "WorkoutInput{" +
                "name='" + name + '\'' +
                ", muscleGroups=" + muscleGroups +
                ", zonedDateTime='" + zonedDateTime + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
