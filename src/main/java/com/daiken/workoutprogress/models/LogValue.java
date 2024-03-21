package com.daiken.workoutprogress.models;

import com.daiken.workoutprogress.api.graphql.input.LogValueInput;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document
@AllArgsConstructor
@NoArgsConstructor
public class LogValue {

    private Integer base;
    private Integer fraction;
    private LogUnit unit;

    public LogValue(LogValueInput input) {
        this.base = input.base();
        this.fraction = input.fraction();
        this.unit = input.unit();
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
