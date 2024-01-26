package com.daiken.workoutprogress.models;

import com.daiken.workoutprogress.api.graphql.input.WorkoutInput;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Document
public class Workout implements Comparable<Workout> {

    @Id
    public String id;

    String name;
    public List<MuscleGroup> muscleGroups;

    @DBRef(lazy = true)
    public User user;

    public LocalDateTime startDateTime;
    public LocalDateTime endDateTime;

    public boolean active;

    public String remark;

    public Workout(String id, String name, List<MuscleGroup> muscleGroups) {
        this.id = id;
        this.name = name;
        this.muscleGroups = muscleGroups;
    }

    public Workout(WorkoutInput input, User user, boolean active) {
        this.user = user;
        this.active = active;
        update(input);
    }

    public Workout update(WorkoutInput input) {
        this.name = input.getName();
        this.muscleGroups = input.getMuscleGroups();
        this.startDateTime = ZonedDateTime.parse(input.getZonedDateTime()).toLocalDateTime();
        this.remark = input.getRemark();
        return this;
    }

    public Workout() {
    }

    public Workout endWorkout(LocalDateTime localDateTime) {
        this.endDateTime = localDateTime;
        this.active = false;
        return this;
    }

    @Override
    public int compareTo(@NotNull Workout o) {
        return o.startDateTime.compareTo(startDateTime);
    }
}