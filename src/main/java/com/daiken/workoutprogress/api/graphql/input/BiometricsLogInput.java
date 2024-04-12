package com.daiken.workoutprogress.api.graphql.input;

import com.daiken.workoutprogress.models.BiometricsType;
import com.daiken.workoutprogress.models.ExternalHealthProvider;
import com.daiken.workoutprogress.models.LogUnit;

/**
 * Input for creating a biometrics log
 *
 * @param type       biometrics type (e.g. weight)
 * @param value      value of the biometrics
 * @param unit       unit of the value (e.g. kg, cm, etc.)
 * @param logDate    date of the log
 * @param dataSource external health provider that provided the data (e.g. Apple Health)
 */
public record BiometricsLogInput(BiometricsType type, Float value, LogUnit unit, String logDate,
                                 ExternalHealthProvider dataSource) {
}
