package com.daiken.workoutprogress.models;

import com.daiken.workoutprogress.api.graphql.input.ExerciseLogInput;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@Document
public class ExerciseLog {

    @Id
    private String id;

    private LocalDateTime logDateTime;

    @DBRef(lazy = true)
    private Exercise exercise;

    @DBRef(lazy = true)
    private Workout workout;

    @DBRef(lazy = true)
    private User user;

    private long repetitions;

    private LogValue logValue;

    private Boolean warmup;

    private String remark;

    private Integer effort;

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
        if (input != null) {
            update(input);
        }
    }

    public void update(ExerciseLogInput input) {
        if (input.zonedDateTimeString() != null) {
            this.logDateTime = ZonedDateTime.parse(input.zonedDateTimeString()).toLocalDateTime();
        }
        this.repetitions = input.repetitions();
        if (input.logValue() != null) {
            this.logValue = new LogValue(input.logValue());
        }
        if (input.warmup() != null) {
            this.warmup = input.warmup();
        }
        if (input.remark() != null) {
            this.remark = input.remark();
        }
        if (input.effort() != null) {
            this.effort = input.effort();
        }
    }

    @Override
    public String toString() {
        // All attributes are included in the toString method for debugging purposes with null checks
        return "ExerciseLog{" +
                "id='" + id + '\'' +
                ", logDateTime=" + logDateTime +
                ", exercise=" + (exercise != null ? exercise.getId() : null) +
                ", workout=" + (workout != null ? workout.getId() : null) +
                ", user=" + (user != null ? user.getId() : null) +
                ", repetitions=" + repetitions +
                ", logValue=" + logValue +
                ", warmup=" + warmup +
                ", remark='" + remark + '\'' +
                '}';
    }
}
