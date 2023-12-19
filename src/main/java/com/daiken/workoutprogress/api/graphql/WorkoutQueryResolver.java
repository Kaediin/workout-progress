package com.daiken.workoutprogress.api.graphql;

import com.daiken.workoutprogress.models.User;
import com.daiken.workoutprogress.models.Workout;
import com.daiken.workoutprogress.repositories.WorkoutRepository;
import com.daiken.workoutprogress.services.UserService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class WorkoutQueryResolver implements GraphQLQueryResolver {

    private final UserService userService;
    private final WorkoutRepository workoutRepository;

    @Autowired
    public WorkoutQueryResolver(
            UserService userService,
            WorkoutRepository workoutRepository
    ) {
        this.userService = userService;
        this.workoutRepository = workoutRepository;
    }

    @PreAuthorize("isAuthenticated()")
    public List<Workout> myWorkouts() {
        User me = userService.getContextUser();
        List<Workout> workouts = workoutRepository.findWorkoutByUserId(me.getId());
        Collections.sort(workouts);
        return workouts;
    }

    @PreAuthorize("isAuthenticated()")
    public Boolean meHasActiveWorkout() {
        User me = userService.getContextUser();
        long workouts = workoutRepository.countWorkoutsByUserAndActive(me, true);
        return workouts > 0;
    }

    @PreAuthorize("isAuthenticated()")
    public Workout workoutById(String id) {
        return workoutRepository.findById(id).orElse(null);
    }
}
