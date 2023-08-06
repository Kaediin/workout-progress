package com.daiken.workoutprogress.api.graphql.input;

import com.daiken.workoutprogress.model.LogUnit;

public class PreferenceInput {
    public LogUnit distanceUnit;
    public LogUnit weightUnit;

    @Deprecated(forRemoval = true)
    public LogUnit unit;
    public int defaultRepetitions;
    public boolean hideUnitSelector;
    public boolean autoAdjustWorkoutMuscleGroups;
}
