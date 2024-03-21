package com.daiken.workoutprogress.api.graphql.input;

import com.daiken.workoutprogress.models.LogUnit;

public record PreferenceInput(
        LogUnit distanceUnit,
        LogUnit weightUnit,
        int defaultRepetitions,
        boolean hideUnitSelector,
        boolean autoAdjustWorkoutMuscleGroups,
        int timerDuration,
        boolean autoStartTimer,
        boolean playTimerCompletionSound) {
}
