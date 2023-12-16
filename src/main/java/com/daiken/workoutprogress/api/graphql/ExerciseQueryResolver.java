package com.daiken.workoutprogress.api.graphql;

import com.daiken.workoutprogress.model.Exercise;
import com.daiken.workoutprogress.model.User;
import com.daiken.workoutprogress.repository.ExerciseRepository;
import com.daiken.workoutprogress.services.UserService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExerciseQueryResolver implements GraphQLQueryResolver {

    private final ExerciseRepository exerciseRepository;
    private final UserService userService;

    @Autowired
    public ExerciseQueryResolver(ExerciseRepository exerciseRepository,
                                 UserService userService) {
        this.exerciseRepository = exerciseRepository;
        this.userService = userService;
    }

    @PreAuthorize("isAuthenticated()")
    public List<Exercise> myExercises() {
        User me = userService.getContextUser();
        return exerciseRepository.findAllByUserId(me.getId());
    }
}
