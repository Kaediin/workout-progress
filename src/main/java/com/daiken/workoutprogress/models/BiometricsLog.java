package com.daiken.workoutprogress.models;

import com.daiken.workoutprogress.api.graphql.input.BiometricsLogInput;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Getter
@Setter
@Document
public class BiometricsLog {

    @Id
    private String id;

    @DBRef(lazy = true)
    private User user;

    private Double value;

    private BiometricsType type;

    private LocalDateTime logDate;

    private LogUnit unit;

    private ExternalHealthProvider dataSource;

    public BiometricsLog() {
    }

    public BiometricsLog(User user, BiometricsLogInput input) {
        this.user = user;
        // Round value to 2 decimal places
        this.value = input.value() != null ? Math.round(input.value() * 100.0) / 100.0 : null;
        this.type = input.type();
        this.unit = input.unit();
        this.logDate = ZonedDateTime.parse(input.logDate()).toLocalDateTime();
        this.dataSource = input.dataSource();
    }
}
