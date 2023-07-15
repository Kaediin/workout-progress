package com.daiken.workoutprogress.api.graphql;

import com.daiken.workoutprogress.api.graphql.input.ExerciseInput;
import com.daiken.workoutprogress.model.Exercise;
import com.daiken.workoutprogress.model.User;
import com.daiken.workoutprogress.repository.ExerciseRepository;
import com.daiken.workoutprogress.services.UserService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Component
public class ExerciseMutationResolver implements GraphQLMutationResolver {

    ExerciseRepository exerciseRepository;
    UserService userService;

    @Autowired
    public ExerciseMutationResolver(ExerciseRepository exerciseRepository,
                                    UserService userService) {
        this.exerciseRepository = exerciseRepository;
        this.userService = userService;
    }

    @PreAuthorize("isAuthenticated()")
    public Exercise createExercise(ExerciseInput input) {
        User me = userService.getContextUser();
        return exerciseRepository.save(new Exercise(input, me));
    }
}
