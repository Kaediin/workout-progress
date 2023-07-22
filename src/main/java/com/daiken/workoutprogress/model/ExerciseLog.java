package com.daiken.workoutprogress.model;

import com.daiken.workoutprogress.api.graphql.input.ExerciseLogInput;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Document
public class ExerciseLog {

    @Id
    String id;

    public LocalDateTime logDateTime;

    @DBRef(lazy = true)
    public Exercise exercise;

    @DBRef(lazy = true)
    public Workout workout;

    @DBRef(lazy = true)
    User user;

    long repetitions;

    double weightLeft;
    double weightRight;

    WeightUnit unit;

    Boolean warmup;

    String remark;

    public ExerciseLog() {
    }

    public ExerciseLog(ExerciseLogInput input, User user, Workout workout, Exercise exercise) {
        this.user = user;
        this.exercise = exercise;
        this.workout = workout;
        update(input);
    }

    public void update(ExerciseLogInput input) {
        this.logDateTime = ZonedDateTime.parse(input.zonedDateTimeString).toLocalDateTime();
        this.repetitions = input.repetitions;
        this.weightLeft = input.weightLeft;
        this.weightRight = input.weightRight;
        this.unit = input.unit;
        this.warmup = input.warmup;
        this.remark = input.remark;
    }
}
