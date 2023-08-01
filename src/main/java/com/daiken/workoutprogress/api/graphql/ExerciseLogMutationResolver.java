package com.daiken.workoutprogress.api.graphql;

import com.daiken.workoutprogress.api.graphql.input.ExerciseLogInput;
import com.daiken.workoutprogress.model.*;
import com.daiken.workoutprogress.repository.ExerciseLogRepository;
import com.daiken.workoutprogress.repository.ExerciseRepository;
import com.daiken.workoutprogress.repository.WorkoutRepository;
import com.daiken.workoutprogress.services.UserService;
import com.daiken.workoutprogress.services.WorkoutService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExerciseLogMutationResolver implements GraphQLMutationResolver {

    private final ExerciseLogRepository exerciseLogRepository;
    private final ExerciseRepository exerciseRepository;
    private final UserService userService;
    private final WorkoutRepository workoutRepository;
    private final WorkoutService workoutService;

    @Autowired
    public ExerciseLogMutationResolver(ExerciseLogRepository exerciseLogRepository,
                                       ExerciseRepository exerciseRepository,
                                       UserService userService,
                                       WorkoutRepository workoutRepository,
                                       WorkoutService workoutService) {
        this.exerciseLogRepository = exerciseLogRepository;
        this.exerciseRepository = exerciseRepository;
        this.userService = userService;
        this.workoutRepository = workoutRepository;
        this.workoutService = workoutService;
    }

    @PreAuthorize("isAuthenticated()")
    public Workout addExerciseLog(String workoutId, ExerciseLogInput input, Boolean autoAdjust) {
        User me = userService.getContextUser();
        if (me == null) {
            throw new NullPointerException("Me not found!");
        }

        Workout currentWorkout = workoutRepository.findById(workoutId).orElseThrow(() -> new NullPointerException("Workout not found with given id"));
        Exercise exercise = exerciseRepository.findById(input.exerciseId).orElseThrow(() -> new NullPointerException("Exercise not found with given id"));
        exerciseLogRepository.save(new ExerciseLog(input, me, currentWorkout, exercise));


        if (autoAdjust) {
            List<MuscleGroup> newGroups = exercise.primaryMuscles.stream().filter(it -> !currentWorkout.muscleGroups.contains(it)).toList();
            if (!newGroups.isEmpty()) {
                currentWorkout.muscleGroups.addAll(newGroups);
                workoutRepository.save(currentWorkout);
            }
        }

        return currentWorkout;
    }

    @PreAuthorize("isAuthenticated()")
    public Workout updateExerciseLog(String exerciseLogId, ExerciseLogInput input) {
        User me = userService.getContextUser();
        if (me == null) {
            throw new NullPointerException("Me not found!");
        }
        ExerciseLog exerciseLog = exerciseLogRepository.findById(exerciseLogId).orElseThrow(() -> new NullPointerException("Can't find exerciselog with given id"));
        exerciseLog.update(input);
        exerciseLogRepository.save(exerciseLog);
        return workoutRepository.findById(exerciseLog.workout.id).orElse(null);
    }

    @PreAuthorize("isAuthenticated()")
    public Boolean removeExerciseLog(String exerciseLogId, boolean autoAdjust) {
        ExerciseLog exerciseLog = exerciseLogRepository.findById(exerciseLogId).orElseThrow(() -> new NullPointerException("ExerciseLog not found with given id"));
        exerciseLogRepository.delete(exerciseLog);
        if (autoAdjust) {
            workoutService.adjustWorkoutMuscleGroups(exerciseLog.workout.id);
        }
        return true;
    }

}