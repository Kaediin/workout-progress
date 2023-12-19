package com.daiken.workoutprogress.api.graphql.input;

import com.daiken.workoutprogress.models.LogUnit;

public class LogValueInput {

    private Integer base;
    private Integer fraction;
    private LogUnit unit;

    public Integer getBase() {
        return base;
    }

    public void setBase(Integer base) {
        this.base = base;
    }

    public Integer getFraction() {
        return fraction;
    }

    public void setFraction(Integer fraction) {
        this.fraction = fraction;
    }

    public LogUnit getUnit() {
        return unit;
    }

    public void setUnit(LogUnit unit) {
        this.unit = unit;
    }
}
