package com.daiken.workoutprogress.models;


import com.daiken.workoutprogress.api.graphql.input.PreferenceInput;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Preference {

    @Id
    public String id;

    @Indexed(unique = true)
    @DBRef(lazy = true)
    public User user;

    public LogUnit distanceUnit;
    public LogUnit weightUnit;

    public int defaultRepetitions;
    public boolean hideUnitSelector;

    public boolean autoAdjustWorkoutMuscleGroups;

    public int timerDuration;
    public boolean autoStartTimer;

    public Preference() {
    }

    public Preference(User user) {
        this.user = user;
        this.weightUnit = LogUnit.KG;
        this.distanceUnit = LogUnit.KM;
        this.defaultRepetitions = 10;
        this.timerDuration = 120; // 120 seconds
        this.autoStartTimer = false;
    }

    public Preference(User user, PreferenceInput input) {
        this.user = user;
        this.update(input);
    }

    public void update(PreferenceInput input) {
        this.weightUnit = input.getWeightUnit();
        this.distanceUnit = input.getDistanceUnit();
        this.defaultRepetitions = input.getDefaultRepetitions();
        this.hideUnitSelector = input.isHideUnitSelector();
        this.autoAdjustWorkoutMuscleGroups = input.isAutoAdjustWorkoutMuscleGroups();
        this.timerDuration = input.getTimerDuration();
        this.autoStartTimer = input.isAutoStartTimer();
    }
}
