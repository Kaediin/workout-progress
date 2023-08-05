package com.daiken.workoutprogress.api.graphql;

import com.daiken.workoutprogress.model.ExerciseLog;
import com.daiken.workoutprogress.model.User;
import com.daiken.workoutprogress.repository.ExerciseLogRepository;
import com.daiken.workoutprogress.repository.ExerciseRepository;
import com.daiken.workoutprogress.services.UserService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExerciseLogQueryResolver implements GraphQLQueryResolver {

    private final ExerciseLogRepository exerciseLogRepository;
    private final ExerciseRepository exerciseRepository;
    private final UserService userService;

    @Autowired
    public ExerciseLogQueryResolver(ExerciseLogRepository exerciseLogRepository,
                                    ExerciseRepository exerciseRepository,
                                    UserService userService) {
        this.exerciseLogRepository = exerciseLogRepository;
        this.exerciseRepository = exerciseRepository;
        this.userService = userService;
    }

    public List<ExerciseLog> latestLogsByExerciseId(String exerciseLogId) {
        User me = userService.getContextUser();
        ExerciseLog latestLoggedExercise = exerciseLogRepository.findLastLogByUserIdAndExerciseId(me.id, exerciseLogId).orElse(null);
        if (latestLoggedExercise == null || latestLoggedExercise.workout.id == null) return null;

        return exerciseLogRepository
                .findLastLogsByWorkoutIdAndExerciseId(latestLoggedExercise.workout.id, exerciseLogId)
                .stream()
                .peek(it -> it.exercise = exerciseRepository.findById(it.exercise.id).orElse(null))
                .filter(it -> it.exercise != null)
                .collect(Collectors.toList());
    }
}
