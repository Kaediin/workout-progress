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
    public Double weightLeft;
    @Deprecated(forRemoval = true)
    public Double weightRight;

    @Deprecated(forRemoval = true)
    public LogValue weightValueLeft;
    @Deprecated(forRemoval = true)
    public LogValue weightValueRight;

    public LogValue logValue;


    @Deprecated(forRemoval = true)
    LogUnit unit;

    Boolean warmup;

    String remark;

    public ExerciseLog() {
    }

    public ExerciseLog(ExerciseLog copyLog, String zonedDateTimeString) {
        this.user = copyLog.user;
        this.exercise = copyLog.exercise;
        this.workout = copyLog.workout;
        this.logDateTime = ZonedDateTime.parse(zonedDateTimeString).toLocalDateTime();
        this.repetitions = copyLog.repetitions;
        this.logValue = copyLog.logValue;
        this.warmup = copyLog.warmup;
        this.remark = copyLog.remark;
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
        this.logValue = new LogValue(input.logValue);
        this.warmup = input.warmup;
        this.remark = input.remark;
    }

    public ExerciseLog convertWeightToWeightValue() {
        String[] weightFraction = String.valueOf(this.weightLeft).split("\\.");
        LogValue value = new LogValue(
                Integer.parseInt(weightFraction[0]),
                Integer.parseInt(weightFraction[1]),
                this.unit
        );
        this.weightValueLeft = value;
        this.weightValueRight = value;
        return this;
    }

    public ExerciseLog convertWeightToLogValue() {
        this.logValue = this.weightValueLeft.convertBaseWeightToBase();
        this.weightValueLeft = null;
        this.weightValueRight = null;
        this.weightLeft = null;
        this.weightRight = null;
        this.unit = null;
        return this;
    }
}
