package com.daiken.workoutprogress.api.graphql;

import com.daiken.workoutprogress.models.Exercise;
import com.daiken.workoutprogress.models.User;
import com.daiken.workoutprogress.repositories.ExerciseRepository;
import com.daiken.workoutprogress.services.ExerciseService;
import com.daiken.workoutprogress.services.UserService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@PreAuthorize("isAuthenticated()")
@Component
public class ExerciseQueryResolver implements GraphQLQueryResolver {

    private final ExerciseRepository exerciseRepository;
    private final ExerciseService exerciseService;
    private final UserService userService;

    @Autowired
    public ExerciseQueryResolver(ExerciseRepository exerciseRepository,
                                 ExerciseService exerciseService,
                                 UserService userService) {
        this.exerciseRepository = exerciseRepository;
        this.exerciseService = exerciseService;
        this.userService = userService;
    }

    public List<Exercise> myExercises() {
        User me = userService.getContextUser();
        return exerciseRepository.findAllByUserId(me.getId());
    }

    public List<Exercise> onboardingExercises() {
        return this.exerciseService.getOnboardingExercises();
    }
}
