package com.daiken.workoutprogress.api.graphql;

import com.daiken.workoutprogress.model.ExerciseLog;
import com.daiken.workoutprogress.model.GroupedExerciseLog;
import com.daiken.workoutprogress.model.User;
import com.daiken.workoutprogress.model.Workout;
import com.daiken.workoutprogress.repository.ExerciseLogRepository;
import com.daiken.workoutprogress.repository.ExerciseRepository;
import com.daiken.workoutprogress.services.UserService;
import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @PreAuthorize("isAuthenticated()")
    public List<ExerciseLog> exerciseLogs(Workout workout) {
        User me = userService.getContextUser();
        return exerciseLogRepository
                .findAllByUserIdAndWorkoutId(me.getId(), workout.id)
                .peek(log -> log.exercise = exerciseRepository.findById(log.exercise.getId()).orElse(null))
                .filter(log -> log.exercise != null)
                .collect(Collectors.toList());

    }

    @PreAuthorize("isAuthenticated()")
    public List<GroupedExerciseLog> groupedExerciseLogs(Workout workout) {
        User me = userService.getContextUser();
        List<ExerciseLog> logs = exerciseLogRepository
                .findAllByUserIdAndWorkoutId(me.getId(), workout.id)
                .peek(log -> log.exercise = exerciseRepository.findById(log.exercise.getId()).orElse(null))
                .filter(log -> log.exercise != null)
                .toList();

        List<GroupedExerciseLog> groupedLogs = new ArrayList<>();

        for (ExerciseLog log : logs) {
            if (groupedLogs.isEmpty() || groupedLogs.stream().noneMatch(groupedLog -> groupedLog.getExercise().getName().equals(log.exercise.getName()))) {
                groupedLogs.add(
                        new GroupedExerciseLog(
                                log.exercise,
                                logs.stream().filter(it -> it.exercise.getName().equals(log.exercise.getName())).collect(Collectors.toList())
                        )
                );
            }
        }
        return groupedLogs;
    }
}
