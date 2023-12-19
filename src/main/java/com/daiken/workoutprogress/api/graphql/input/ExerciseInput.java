package com.daiken.workoutprogress.api.graphql.input;

import com.daiken.workoutprogress.models.MuscleGroup;

import java.util.List;

public class ExerciseInput {

    private String name;

    private List<MuscleGroup> primaryMuscles;
    private List<MuscleGroup> secondaryMuscles;

    private LogValueInput defaultAppliedWeight;

    private String notes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MuscleGroup> getPrimaryMuscles() {
        return primaryMuscles;
    }

    public void setPrimaryMuscles(List<MuscleGroup> primaryMuscles) {
        this.primaryMuscles = primaryMuscles;
    }

    public List<MuscleGroup> getSecondaryMuscles() {
        return secondaryMuscles;
    }

    public void setSecondaryMuscles(List<MuscleGroup> secondaryMuscles) {
        this.secondaryMuscles = secondaryMuscles;
    }

    public LogValueInput getDefaultAppliedWeight() {
        return defaultAppliedWeight;
    }

    public void setDefaultAppliedWeight(LogValueInput defaultAppliedWeight) {
        this.defaultAppliedWeight = defaultAppliedWeight;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
