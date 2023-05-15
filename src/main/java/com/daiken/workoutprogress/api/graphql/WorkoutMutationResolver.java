package com.daiken.workoutprogress.api.graphql;

import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;


@Component
public class WorkoutMutationResolver implements GraphQLMutationResolver {

    @Autowired
    public WorkoutMutationResolver() {
    }

    public void meStartWorkout() {
        Authentication auth = getContext().getAuthentication();

        System.out.println(auth);

        return;
    }
}
