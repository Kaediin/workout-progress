package com.daiken.workoutprogress.model;

import org.springframework.data.annotation.Id;

import java.util.List;

public class Workout {

    @Id
    String id;

    String name;

    List<MuscleGroup> muscleGroups;

    public Workout() {
    }

    public Workout(String id, String name, List<MuscleGroup> muscleGroups) {
        this.id = id;
        this.name = name;
        this.muscleGroups = muscleGroups;
    }
}
