package com.daiken.workoutprogress.api.graphql.input;

import com.daiken.workoutprogress.models.LogUnit;

/**
 * Represents the input for creating a log value.
 *
 * @param base     The base of the log value, any integer
 * @param fraction The fraction of the log value (e.g .25, .5, .75)
 * @param unit     The unit of the log value (e.g. KG, LBS)
 */
public record LogValueInput(Integer base, Integer fraction, LogUnit unit) {
}
