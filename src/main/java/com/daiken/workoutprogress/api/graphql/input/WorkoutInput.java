package com.daiken.workoutprogress.api.graphql.input;

import com.daiken.workoutprogress.models.MuscleGroup;

import java.util.List;

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
