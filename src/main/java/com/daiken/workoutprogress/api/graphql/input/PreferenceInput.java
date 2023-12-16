package com.daiken.workoutprogress.api.graphql.input;

import com.daiken.workoutprogress.model.LogUnit;

public class PreferenceInput {
    private LogUnit distanceUnit;
    private LogUnit weightUnit;

    private int defaultRepetitions;
    private boolean hideUnitSelector;
    private boolean autoAdjustWorkoutMuscleGroups;
    private int timerDuration;
    private boolean autoStartTimer;

    public LogUnit getDistanceUnit() {
        return distanceUnit;
    }

    public void setDistanceUnit(LogUnit distanceUnit) {
        this.distanceUnit = distanceUnit;
    }

    public LogUnit getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(LogUnit weightUnit) {
        this.weightUnit = weightUnit;
    }

    public int getDefaultRepetitions() {
        return defaultRepetitions;
    }

    public void setDefaultRepetitions(int defaultRepetitions) {
        this.defaultRepetitions = defaultRepetitions;
    }

    public boolean isHideUnitSelector() {
        return hideUnitSelector;
    }

    public void setHideUnitSelector(boolean hideUnitSelector) {
        this.hideUnitSelector = hideUnitSelector;
    }

    public boolean isAutoAdjustWorkoutMuscleGroups() {
        return autoAdjustWorkoutMuscleGroups;
    }

    public void setAutoAdjustWorkoutMuscleGroups(boolean autoAdjustWorkoutMuscleGroups) {
        this.autoAdjustWorkoutMuscleGroups = autoAdjustWorkoutMuscleGroups;
    }

    public int getTimerDuration() {
        return timerDuration;
    }

    public void setTimerDuration(int timerDuration) {
        this.timerDuration = timerDuration;
    }

    public boolean isAutoStartTimer() {
        return autoStartTimer;
    }

    public void setAutoStartTimer(boolean autoStartTimer) {
        this.autoStartTimer = autoStartTimer;
    }
}
