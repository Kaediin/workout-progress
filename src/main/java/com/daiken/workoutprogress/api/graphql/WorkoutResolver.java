package com.daiken.workoutprogress.api.graphql;

import com.daiken.workoutprogress.models.ExerciseLog;
import com.daiken.workoutprogress.models.GroupedExerciseLog;
import com.daiken.workoutprogress.models.User;
import com.daiken.workoutprogress.models.Workout;
import com.daiken.workoutprogress.repositories.ExerciseLogRepository;
import com.daiken.workoutprogress.repositories.ExerciseRepository;
import com.daiken.workoutprogress.services.UserService;
import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@PreAuthorize("isAuthenticated()")
@Component
public class WorkoutResolver implements GraphQLResolver<Workout> {

    private final UserService userService;
    private final ExerciseLogRepository exerciseLogRepository;
    private final ExerciseRepository exerciseRepository;

    @Autowired
    public WorkoutResolver(UserService userService,
                           ExerciseLogRepository exerciseLogRepository,
                           ExerciseRepository exerciseRepository) {
        this.exerciseLogRepository = exerciseLogRepository;
        this.exerciseRepository = exerciseRepository;
        this.userService = userService;
    }

    public List<ExerciseLog> exerciseLogs(Workout workout) {
        User me = userService.getContextUser();
        return exerciseLogRepository
                .findAllByUserIdAndWorkoutId(me.getId(), workout.getId())
                .peek(log -> log.setExercise(exerciseRepository.findById(log.getExercise().getId()).orElse(null)))
                .filter(log -> log.getExercise() != null)
                .collect(Collectors.toList());

    }

    public List<GroupedExerciseLog> groupedExerciseLogs(Workout workout) {
        User me = userService.getContextUser();
        List<ExerciseLog> logs = exerciseLogRepository
                .findAllByUserIdAndWorkoutId(me.getId(), workout.getId())
                .peek(log -> log.setExercise(exerciseRepository.findById(log.getExercise().getId()).orElse(null)))
                .filter(log -> log.getExercise() != null)
                .toList();

        List<GroupedExerciseLog> groupedLogs = new ArrayList<>();

        for (ExerciseLog log : logs) {
            if (groupedLogs.isEmpty() || groupedLogs.stream().noneMatch(groupedLog -> groupedLog.exercise().getName().equals(log.getExercise().getName()))) {
                groupedLogs.add(
                        new GroupedExerciseLog(
                                log.getExercise(),
                                logs.stream().filter(it -> it.getExercise().getName().equals(log.getExercise().getName())).collect(Collectors.toList())
                        )
                );
            }
        }
        return groupedLogs;
    }
}
