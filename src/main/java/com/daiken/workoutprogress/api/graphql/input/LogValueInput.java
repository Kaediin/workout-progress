package com.daiken.workoutprogress.api.graphql.input;

import com.daiken.workoutprogress.models.LogUnit;

public record LogValueInput(Integer base, Integer fraction, LogUnit unit) {
}
