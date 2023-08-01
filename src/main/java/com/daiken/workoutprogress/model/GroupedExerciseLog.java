package com.daiken.workoutprogress.model;

import java.util.List;

public class GroupedExerciseLog {

    public Exercise exercise;
    List<ExerciseLog> logs;

    public GroupedExerciseLog() {
    }

    public GroupedExerciseLog(Exercise exercise, List<ExerciseLog> logs) {
        this.exercise = exercise;
        this.logs = logs;
    }
}
