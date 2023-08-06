package com.daiken.workoutprogress.model;

import com.daiken.workoutprogress.api.graphql.input.LogValueInput;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class LogValue {

    @Deprecated(forRemoval = true)
    public Integer baseWeight;

    public Integer base;
    public Integer fraction;
    public LogUnit unit;

    public LogValue(Integer base, Integer fraction, LogUnit unit) {
        this.base = base;
        this.fraction = fraction;
        this.unit = unit;
    }

    public LogValue(LogValueInput input) {
        this.base = input.base;
        this.fraction = input.fraction;
        this.unit = input.unit;
    }

    public LogValue() {
    }

    public LogValue convertBaseWeightToBase() {
        this.base = this.baseWeight;
        this.baseWeight = null;
        return this;
    }
}
