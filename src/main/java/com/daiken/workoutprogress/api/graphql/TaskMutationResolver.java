package com.daiken.workoutprogress.api.graphql;

import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@PreAuthorize("isAuthenticated()")
@Component
public class TaskMutationResolver implements GraphQLMutationResolver {

    public TaskMutationResolver() {
    }

    public Boolean runFetchWorkoutsTask() {
        return true;
    }
}
