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

    @Deprecated(forRemoval = true)
    public double weightLeft;
    @Deprecated(forRemoval = true)
    public double weightRight;

    public WeightValue weightValueLeft;
    public WeightValue weightValueRight;

    @Deprecated(forRemoval = true)
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
        this.weightValueLeft = new WeightValue(input.weightLeft);
        this.weightValueRight = new WeightValue(input.weightRight);
        this.warmup = input.warmup;
        this.remark = input.remark;
    }

    public ExerciseLog convertWeightToWeightValue() {
        String[] weightFraction = String.valueOf(this.weightLeft).split("\\.");
        WeightValue value = new WeightValue(
                Integer.parseInt(weightFraction[0]),
                Integer.parseInt(weightFraction[1]),
                this.unit
        );
        this.weightValueLeft = value;
        this.weightValueRight = value;
        return this;
    }
}
