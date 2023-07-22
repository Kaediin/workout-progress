package com.daiken.workoutprogress.api.graphql.input;

import com.daiken.workoutprogress.model.MuscleGroup;

import java.util.List;

public class ExerciseInput {

    public String name;

    public List<MuscleGroup> primaryMuscles;
    public List<MuscleGroup> secondaryMuscles;

    public WeightValueInput defaultAppliedWeight;
}
