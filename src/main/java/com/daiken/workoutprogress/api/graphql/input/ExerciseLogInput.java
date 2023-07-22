package com.daiken.workoutprogress.api.graphql.input;

import com.daiken.workoutprogress.model.WeightUnit;

public class ExerciseLogInput {

    public String zonedDateTimeString;
    public String exerciseId;
    public long repetitions;

    public double weightLeft;
    public double weightRight;

    public WeightUnit unit;

    public Boolean warmup;

    public String remark;

    public ExerciseLogInput() {
    }
}
