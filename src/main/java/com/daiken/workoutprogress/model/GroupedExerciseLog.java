package com.daiken.workoutprogress.model;

import java.util.List;

public class GroupedExerciseLog {

    private Exercise exercise;
    private List<ExerciseLog> logs;

    public GroupedExerciseLog() {
    }

    public GroupedExerciseLog(Exercise exercise, List<ExerciseLog> logs) {
        this.exercise = exercise;
        this.logs = logs;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public List<ExerciseLog> getLogs() {
        return logs;
    }

    public void setLogs(List<ExerciseLog> logs) {
        this.logs = logs;
    }
}
