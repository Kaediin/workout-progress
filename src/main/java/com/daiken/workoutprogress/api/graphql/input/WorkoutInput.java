package com.daiken.workoutprogress.api.graphql.input;

import com.daiken.workoutprogress.model.MuscleGroup;

import java.util.List;

public class WorkoutInput {

    public String name;
    public List<MuscleGroup> muscleGroups;
    public String zonedDateTime;
    public String remark;

    public WorkoutInput() {
    }
}
