package com.daiken.workoutprogress.models;

import org.jetbrains.annotations.NotNull;

public record MuscleGroupChartData(MuscleGroup muscleGroup, String color,
                                   int count) implements Comparable<MuscleGroupChartData> {
    @Override
    public int compareTo(@NotNull MuscleGroupChartData o) {
        return muscleGroup.compareTo(o.muscleGroup());
    }
}
