package com.daiken.workoutprogress.api.graphql.input;

import com.daiken.workoutprogress.models.MuscleGroup;

import java.util.List;

public record WorkoutInput(String name, List<MuscleGroup> muscleGroups, String zonedDateTime, String remark) {
}
