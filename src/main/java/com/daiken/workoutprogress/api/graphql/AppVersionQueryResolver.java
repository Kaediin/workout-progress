package com.daiken.workoutprogress.api.graphql;

import graphql.kickstart.tools.GraphQLQueryResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * Resolver for the app version query
 */
@Slf4j
@PreAuthorize("isAuthenticated()")
@Component
public class AppVersionQueryResolver implements GraphQLQueryResolver {

    @Value("${app-version.check}")
    private Boolean checkVersion;

    public AppVersionQueryResolver() {
    }

    /**
     * Check if the app version should be checked in the FE and thus prompt the user to update if version is updated
     *
     * @return true if the app version should be checked, false otherwise
     */
    public Boolean checkAppVersion() {
        return checkVersion != null ? checkVersion : false;
    }
}
