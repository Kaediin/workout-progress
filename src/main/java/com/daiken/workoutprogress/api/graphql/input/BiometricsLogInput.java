package com.daiken.workoutprogress.api.graphql.input;

import com.daiken.workoutprogress.models.BiometricsType;
import com.daiken.workoutprogress.models.ExternalHealthProvider;
import com.daiken.workoutprogress.models.LogUnit;

public record BiometricsLogInput(BiometricsType type, Float value, LogUnit unit, String logDate,
                                 ExternalHealthProvider dataSource) {
}
