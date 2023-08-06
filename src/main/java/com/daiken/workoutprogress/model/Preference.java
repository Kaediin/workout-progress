package com.daiken.workoutprogress.model;


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

    @Deprecated(forRemoval = true)
    public LogUnit unit;
    public int defaultRepetitions;
    public boolean hideUnitSelector;

    public boolean autoAdjustWorkoutMuscleGroups;

    public Preference() {
    }

    public Preference(User user) {
        this.user = user;
        this.weightUnit = LogUnit.KG;
        this.distanceUnit = LogUnit.KM;
        this.defaultRepetitions = 10;
    }

    public Preference(User user, PreferenceInput input) {
        this.user = user;
        this.update(input);
    }

    public void update(PreferenceInput input) {
        this.weightUnit = input.weightUnit;
        this.distanceUnit = input.distanceUnit;
        this.defaultRepetitions = input.defaultRepetitions;
        this.hideUnitSelector = input.hideUnitSelector;
        this.autoAdjustWorkoutMuscleGroups = input.autoAdjustWorkoutMuscleGroups;
    }
}
