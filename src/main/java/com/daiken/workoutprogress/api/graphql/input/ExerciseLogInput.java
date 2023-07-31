package com.daiken.workoutprogress.api.graphql.input;

public class ExerciseLogInput {

    public String zonedDateTimeString;
    public String exerciseId;
    public long repetitions;

    public WeightValueInput weightLeft;
    public WeightValueInput weightRight;

    public Boolean warmup;

    public String remark;

    public ExerciseLogInput() {
    }
}
