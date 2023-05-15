package com.daiken.workoutprogress.api.graphql;

import com.daiken.workoutprogress.tasks.FetchWorkoutsTask;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskMutationResolver implements GraphQLMutationResolver {

    private final FetchWorkoutsTask fetchWorkoutsTask;

    @Autowired
    public TaskMutationResolver(FetchWorkoutsTask fetchWorkoutsTask) {
        this.fetchWorkoutsTask = fetchWorkoutsTask;
    }

    public Boolean runFetchWorkoutsTask() {
        this.fetchWorkoutsTask.runAsync();
        return true;
    }
}
