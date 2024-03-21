package com.daiken.workoutprogress.api.graphql.input;

import com.daiken.workoutprogress.models.MuscleGroup;

import java.util.List;

public record ExerciseInput(String name, List<MuscleGroup> primaryMuscles, List<MuscleGroup> secondaryMuscles,
                            LogValueInput defaultAppliedWeight, String notes) {

}
