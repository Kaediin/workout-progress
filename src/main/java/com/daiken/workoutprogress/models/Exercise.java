package com.daiken.workoutprogress.models;

import com.daiken.workoutprogress.api.graphql.input.ExerciseInput;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Objects;

@Document
public class Exercise {

    @Id
    public String id;

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

    public Exercise() {
    }

    public void update(ExerciseInput input) {
        this.name = input.getName();
        this.primaryMuscles = input.getPrimaryMuscles();
        this.secondaryMuscles = input.getSecondaryMuscles();
        this.notes = input.getNotes();
        if (input.getDefaultAppliedWeight() != null) {
            this.defaultAppliedWeight = new LogValue(input.getDefaultAppliedWeight());
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

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setPrimaryMuscles(List<MuscleGroup> primaryMuscles) {
        this.primaryMuscles = primaryMuscles;
    }

    public void setSecondaryMuscles(List<MuscleGroup> secondaryMuscles) {
        this.secondaryMuscles = secondaryMuscles;
    }

    public void setDefaultAppliedWeight(LogValue defaultAppliedWeight) {
        this.defaultAppliedWeight = defaultAppliedWeight;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getName() {
        return name;
    }

    public User getUser() {
        return user;
    }

    public List<MuscleGroup> getPrimaryMuscles() {
        return primaryMuscles;
    }

    public List<MuscleGroup> getSecondaryMuscles() {
        return secondaryMuscles;
    }

    public LogValue getDefaultAppliedWeight() {
        return defaultAppliedWeight;
    }

    public String getNotes() {
        return notes;
    }
}
