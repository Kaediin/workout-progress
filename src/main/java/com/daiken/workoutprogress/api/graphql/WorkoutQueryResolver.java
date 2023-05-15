package com.daiken.workoutprogress.api.graphql;

import com.daiken.workoutprogress.model.MuscleGroup;
import com.daiken.workoutprogress.model.Workout;
import com.daiken.workoutprogress.repository.WorkoutRepository;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@Component
public class WorkoutQueryResolver implements GraphQLQueryResolver {

    private final WorkoutRepository workoutRepository;

    @Autowired
    public WorkoutQueryResolver(WorkoutRepository workoutRepository) {
        this.workoutRepository = workoutRepository;
    }

    public List<Workout> workouts() {
        Authentication user = getContext().getAuthentication();

        return List.of(new Workout("0", "Bench press", List.of(MuscleGroup.CHEST, MuscleGroup.TRICEPS)));
    }
}
