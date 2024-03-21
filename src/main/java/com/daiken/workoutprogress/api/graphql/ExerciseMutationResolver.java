package com.daiken.workoutprogress.api.graphql;

import com.daiken.workoutprogress.api.graphql.input.ExerciseInput;
import com.daiken.workoutprogress.models.Exercise;
import com.daiken.workoutprogress.models.ExerciseLog;
import com.daiken.workoutprogress.models.User;
import com.daiken.workoutprogress.repositories.ExerciseLogRepository;
import com.daiken.workoutprogress.repositories.ExerciseRepository;
import com.daiken.workoutprogress.services.UserService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.List;

@PreAuthorize("isAuthenticated()")
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

    public Exercise createExercise(ExerciseInput input) {
        User me = userService.getContextUser();
        return exerciseRepository.save(new Exercise(input, me));
    }

    public Exercise updateExercise(String id, ExerciseInput input) {
        Exercise exercise = exerciseRepository.findById(id).orElseThrow(() -> new NullPointerException("Exercise not found with given id"));
        exercise.update(input);
        return exerciseRepository.save(exercise);
    }

    public Boolean deleteExercise(String id) {
        User me = userService.getContextUser();
        Exercise exercise = exerciseRepository.findById(id).orElseThrow(() -> new NullPointerException("Exercise not found with given id"));
        List<ExerciseLog> logs = exerciseLogRepository.findAllByUserIdAndExerciseId(me.getId(), exercise.getId());
        exerciseLogRepository.deleteAll(logs);
        exerciseRepository.delete(exercise);
        return true;
    }
}
