package com.daiken.workoutprogress.model;

import com.daiken.workoutprogress.api.graphql.input.ExerciseInput;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public class Exercise {

    @Id
    public String id;

    public String name;

    @DBRef(lazy = true)
    User user;

    public List<MuscleGroup> primaryMuscles;
    public List<MuscleGroup> secondaryMuscles;

    public LogValue defaultAppliedWeight;

    public String notes;

    public Exercise(ExerciseInput input, User me) {
        this.user = me;
        update(input);
    }

    public Exercise() {
    }

    public void update(ExerciseInput input) {
        this.name = input.name;
        this.primaryMuscles = input.primaryMuscles;
        this.secondaryMuscles = input.secondaryMuscles;
        this.notes = input.notes;
        if (input.defaultAppliedWeight != null) {
            this.defaultAppliedWeight = new LogValue(input.defaultAppliedWeight);
        }
    }
}
