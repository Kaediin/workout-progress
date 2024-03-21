package com.daiken.workoutprogress.models;

import java.util.List;

public record GroupedExerciseLog(Exercise exercise, List<ExerciseLog> logs) {
}
