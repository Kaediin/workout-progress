package com.daiken.workoutprogress.api.graphql.input;

import com.daiken.workoutprogress.model.WeightUnit;

public class PreferenceInput {
    public WeightUnit unit;
    public int defaultRepetitions;
    public boolean hideUnitSelector;
    public boolean autoAdjustWorkoutMuscleGroups;
}
