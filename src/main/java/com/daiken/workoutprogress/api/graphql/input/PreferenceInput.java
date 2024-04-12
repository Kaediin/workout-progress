package com.daiken.workoutprogress.api.graphql.input;

import com.daiken.workoutprogress.models.LogUnit;

/**
 * Represents the input for creating or updating a preference.
 *
 * @param distanceUnit                  The unit of distance to use (e.g. KM, MI)
 * @param weightUnit                    The unit of weight to use (e.g. KG, LBS)
 * @param defaultRepetitions            The default number of repetitions
 * @param hideUnitSelector              Whether to hide the unit selector when logging
 * @param autoAdjustWorkoutMuscleGroups Whether to automatically adjust the workout muscle groups based on the exercise muscle groups
 * @param timerDuration                 The duration of the timer in seconds
 * @param autoStartTimer                Whether to automatically start the timer when logging
 * @param playTimerCompletionSound      Whether to play a sound when the timer completes
 */
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
