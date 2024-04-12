package com.daiken.workoutprogress.api.graphql.input;

import com.daiken.workoutprogress.models.MuscleGroup;

import java.util.List;

/**
 * Represents the input for creating or updating an exercise.
 *
 * @param name                 The name of the exercise
 * @param primaryMuscles       The primary muscle groups worked by the exercise
 * @param secondaryMuscles     The secondary muscle groups worked by the exercise
 * @param defaultAppliedWeight The default applied weight for the exercise
 * @param notes                Any notes about the exercise
 */
public record ExerciseInput(String name, List<MuscleGroup> primaryMuscles, List<MuscleGroup> secondaryMuscles,
                            LogValueInput defaultAppliedWeight, String notes) {

}
