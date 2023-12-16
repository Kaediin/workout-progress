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

    public LogValue logValue;

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
        if (input.getZonedDateTimeString() != null) {
            this.logDateTime = ZonedDateTime.parse(input.getZonedDateTimeString()).toLocalDateTime();
        }
        this.repetitions = input.repetitions;
        if (input.logValue != null) {
            this.logValue = new LogValue(input.logValue);
        }
        if (input.warmup != null) {
            this.warmup = input.warmup;
        }
        if (input.remark != null) {
            this.remark = input.remark;
        }
    }
}
