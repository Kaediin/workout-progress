package com.daiken.workoutprogress.models;


import com.daiken.workoutprogress.api.graphql.input.PreferenceInput;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document
@NoArgsConstructor
public class Preference {

    @Id
    private String id;

    @Indexed(unique = true)
    @DBRef(lazy = true)
    private User user;

    private LogUnit distanceUnit;
    private LogUnit weightUnit;

    private int defaultRepetitions;
    private boolean hideUnitSelector;

    private boolean autoAdjustWorkoutMuscleGroups;

    private int timerDuration;
    private boolean autoStartTimer;
    private boolean playTimerCompletionSound;

    public Preference(User user) {
        this.user = user;
        this.weightUnit = LogUnit.KG;
        this.distanceUnit = LogUnit.KM;
        this.defaultRepetitions = 10;
        this.timerDuration = 120; // 120 seconds
        this.autoStartTimer = false;
        this.autoAdjustWorkoutMuscleGroups = true;
        this.playTimerCompletionSound = true;
    }

    public Preference(User user, PreferenceInput input) {
        this.user = user;
        this.update(input);
    }

    public void update(PreferenceInput input) {
        this.weightUnit = input.weightUnit();
        this.distanceUnit = input.distanceUnit();
        this.defaultRepetitions = input.defaultRepetitions();
        this.hideUnitSelector = input.hideUnitSelector();
        this.autoAdjustWorkoutMuscleGroups = true;
        this.timerDuration = input.timerDuration();
        this.autoStartTimer = input.autoStartTimer();
        this.playTimerCompletionSound = input.playTimerCompletionSound();
    }
}
