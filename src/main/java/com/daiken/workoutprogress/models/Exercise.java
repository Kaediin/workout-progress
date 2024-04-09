package com.daiken.workoutprogress.models;

import com.daiken.workoutprogress.api.graphql.input.ExerciseInput;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Objects;


@Getter
@Setter
@Document
public class Exercise {

    @Id
    private String id;

    private String name;

    @DBRef(lazy = true)
    private User user;

    private List<MuscleGroup> primaryMuscles;
    private List<MuscleGroup> secondaryMuscles;

    private LogValue defaultAppliedWeight;

    private String notes;

    public Exercise(ExerciseInput input, User me) {
        this.user = me;
        update(input);
    }

    public Exercise(String id, ExerciseInput input, User me) {
        this.id = id;
        this.user = me;
        update(input);
    }

    public Exercise() {
    }

    public void update(ExerciseInput input) {
        this.name = input.name();
        this.primaryMuscles = input.primaryMuscles();
        this.secondaryMuscles = input.secondaryMuscles();
        this.notes = input.notes();
        if (input.defaultAppliedWeight() != null) {
            this.defaultAppliedWeight = new LogValue(input.defaultAppliedWeight());
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Exercise myObject = (Exercise) obj;
        return id.equals(myObject.id);
    }
}
