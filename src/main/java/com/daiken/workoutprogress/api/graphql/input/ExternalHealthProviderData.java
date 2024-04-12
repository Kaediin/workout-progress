package com.daiken.workoutprogress.api.graphql.input;

import com.daiken.workoutprogress.models.ExternalHealthProvider;

/**
 * Represents the input for creating an external health provider data.
 *
 * @param appleHealthId The Apple Health ID of the log
 * @param provider      The external health provider (e.g. Apple Health)
 */
public record ExternalHealthProviderData(String appleHealthId, ExternalHealthProvider provider) {
}
