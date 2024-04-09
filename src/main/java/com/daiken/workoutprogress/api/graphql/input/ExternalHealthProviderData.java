package com.daiken.workoutprogress.api.graphql.input;

import com.daiken.workoutprogress.models.ExternalHealthProvider;

public record ExternalHealthProviderData(String appleHealthId, ExternalHealthProvider provider) {
}
