package com.daiken.workoutprogress.model;

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
    List<MuscleGroup> muscleGroups;

    @DBRef(lazy = true)
    User user;

    public LocalDateTime startDateTime;
    public LocalDateTime endDateTime;

    public boolean active;

    public Workout(String id, String name, List<MuscleGroup> muscleGroups) {
        this.id = id;
        this.name = name;
        this.muscleGroups = muscleGroups;
    }

    public Workout(WorkoutInput input, User user, boolean active) {
        this.name = input.name;
        this.muscleGroups = input.muscleGroups;
        this.user = user;
        this.active = active;
        this.startDateTime = ZonedDateTime.parse(input.zonedDateTime).toLocalDateTime();
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
