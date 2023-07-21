package com.daiken.workoutprogress.api.graphql;

import com.daiken.workoutprogress.api.graphql.input.ExerciseInput;
import com.daiken.workoutprogress.model.Exercise;
import com.daiken.workoutprogress.model.ExerciseLog;
import com.daiken.workoutprogress.model.User;
import com.daiken.workoutprogress.repository.ExerciseLogRepository;
import com.daiken.workoutprogress.repository.ExerciseRepository;
import com.daiken.workoutprogress.services.UserService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExerciseMutationResolver implements GraphQLMutationResolver {

    ExerciseLogRepository exerciseLogRepository;
    ExerciseRepository exerciseRepository;
    UserService userService;

    @Autowired
    public ExerciseMutationResolver(ExerciseLogRepository exerciseLogRepository,
                                    ExerciseRepository exerciseRepository,
                                    UserService userService) {
        this.exerciseLogRepository = exerciseLogRepository;
        this.exerciseRepository = exerciseRepository;
        this.userService = userService;
    }

    @PreAuthorize("isAuthenticated()")
    public Exercise createExercise(ExerciseInput input) {
        User me = userService.getContextUser();
        return exerciseRepository.save(new Exercise(input, me));
    }

    @PreAuthorize("isAuthenticated()")
    public Exercise updateExercise(String id, ExerciseInput input) {
        User me = userService.getContextUser();
        Exercise exercise = exerciseRepository.findById(id).orElseThrow(() -> new NullPointerException("Exercise not found with given id"));
        exercise.update(input);
        return exerciseRepository.save(exercise);
    }

    @PreAuthorize("isAuthenticated()")
    public Boolean deleteExercise(String id) {
        User me = userService.getContextUser();
        Exercise exercise = exerciseRepository.findById(id).orElseThrow(() -> new NullPointerException("Exercise not found with given id"));
        List<ExerciseLog> logs = exerciseLogRepository.findAllByUserIdAndExerciseId(me.id, exercise.id);
        exerciseLogRepository.deleteAll(logs);
        exerciseRepository.delete(exercise);
        return true;
    }
}
