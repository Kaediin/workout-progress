package com.daiken.workoutprogress.api.graphql;

import com.daiken.workoutprogress.models.ExerciseLog;
import com.daiken.workoutprogress.models.User;
import com.daiken.workoutprogress.models.Workout;
import com.daiken.workoutprogress.repositories.ExerciseLogRepository;
import com.daiken.workoutprogress.repositories.ExerciseRepository;
import com.daiken.workoutprogress.repositories.WorkoutRepository;
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
    private final WorkoutRepository workoutRepository;

    @Autowired
    public ExerciseLogQueryResolver(ExerciseLogRepository exerciseLogRepository,
                                    ExerciseRepository exerciseRepository,
                                    UserService userService,
                                    WorkoutRepository workoutRepository) {
        this.exerciseLogRepository = exerciseLogRepository;
        this.exerciseRepository = exerciseRepository;
        this.userService = userService;
        this.workoutRepository = workoutRepository;
    }

    public List<ExerciseLog> latestLogsByExerciseId(String exerciseLogId) {
        User me = userService.getContextUser();
        ExerciseLog latestLoggedExercise = exerciseLogRepository.findLastLogByUserIdAndExerciseId(me.getId(), exerciseLogId).orElse(null);
        if (latestLoggedExercise == null || latestLoggedExercise.workout.id == null) return null;

        return exerciseLogRepository
                .findLastLogsByWorkoutIdAndExerciseId(latestLoggedExercise.workout.id, exerciseLogId)
                .stream()
                .peek(it -> it.exercise = exerciseRepository.findById(it.exercise.id).orElse(null))
                .filter(it -> it.exercise != null)
                .collect(Collectors.toList());
    }

    public List<ExerciseLog> latestLogsByExerciseIdAndNotWorkoutId(String exerciseLogId, String workoutId) {
        User me = userService.getContextUser();
        Workout workout = workoutRepository.findById(workoutId).orElse(null);
        if (workout == null) return null;
        ExerciseLog latestLoggedExercise = exerciseLogRepository.findFirstByUserIdAndExerciseIdAndWorkoutNotOrderByLogDateTimeDesc(me.getId(), exerciseLogId, workout).orElse(null);
        if (latestLoggedExercise == null || latestLoggedExercise.workout.id == null) return null;

        return exerciseLogRepository
                .findLastLogsByWorkoutIdAndExerciseId(latestLoggedExercise.workout.id, exerciseLogId)
                .stream()
                .peek(it -> it.exercise = exerciseRepository.findById(it.exercise.id).orElse(null))
                .filter(it -> it.exercise != null)
                .collect(Collectors.toList());
    }
}
