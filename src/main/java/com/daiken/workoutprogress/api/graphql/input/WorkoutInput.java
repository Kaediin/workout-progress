package com.daiken.workoutprogress.api.graphql.input;

import com.daiken.workoutprogress.models.MuscleGroup;

import java.util.List;

public class WorkoutInput {

    private String name;
    private List<MuscleGroup> muscleGroups;
    private String zonedDateTime;
    private String remark;

    public WorkoutInput() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MuscleGroup> getMuscleGroups() {
        return muscleGroups;
    }

    public void setMuscleGroups(List<MuscleGroup> muscleGroups) {
        this.muscleGroups = muscleGroups;
    }

    public String getZonedDateTime() {
        return zonedDateTime;
    }

    public void setZonedDateTime(String zonedDateTime) {
        this.zonedDateTime = zonedDateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
