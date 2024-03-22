package com.daiken.workoutprogress.models;

import java.util.List;

public record ExerciseLineChartData(String monthLabel, List<ExerciseLog> logs) {
}
