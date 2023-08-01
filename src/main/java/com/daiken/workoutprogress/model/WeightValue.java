package com.daiken.workoutprogress.model;

import com.daiken.workoutprogress.api.graphql.input.WeightValueInput;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class WeightValue {

    public Integer baseWeight;
    public Integer fraction;
    public WeightUnit unit;

    public WeightValue(Integer baseWeight, Integer fraction, WeightUnit unit) {
        this.baseWeight = baseWeight;
        this.fraction = fraction;
        this.unit = unit;
    }

    public WeightValue(WeightValueInput input) {
        this.baseWeight = input.baseWeight;
        this.fraction = input.fraction;
        this.unit = input.unit;
    }

    public WeightValue() {
    }
}
