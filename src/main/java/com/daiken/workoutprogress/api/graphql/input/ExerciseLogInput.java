package com.daiken.workoutprogress.api.graphql.input;

public record ExerciseLogInput(String zonedDateTimeString, String exerciseId, long repetitions, LogValueInput logValue,
                               Boolean warmup, String remark) {

}
