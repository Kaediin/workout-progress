package com.daiken.workoutprogress.models;

import com.daiken.workoutprogress.api.graphql.input.LogValueInput;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class LogValue {

    private Integer base;
    private Integer fraction;
    private LogUnit unit;

    public LogValue(Integer base, Integer fraction, LogUnit unit) {
        this.base = base;
        this.fraction = fraction;
        this.unit = unit;
    }

    public LogValue(LogValueInput input) {
        this.base = input.getBase();
        this.fraction = input.getFraction();
        this.unit = input.getUnit();
    }

    public LogValue() {
    }

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

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof LogValue)) {
            return false;
        }

        return ((LogValue) obj).getBase().equals(getBase()) &&
                ((LogValue) obj).getFraction().equals(getFraction()) &&
                ((LogValue) obj).getUnit().equals(getUnit());
    }
}
