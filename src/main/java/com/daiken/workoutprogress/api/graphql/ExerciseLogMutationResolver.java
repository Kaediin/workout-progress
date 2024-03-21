package com.daiken.workoutprogress.api.graphql;

import com.daiken.workoutprogress.api.graphql.input.ExerciseLogInput;
import com.daiken.workoutprogress.models.Exercise;
import com.daiken.workoutprogress.models.ExerciseLog;
import com.daiken.workoutprogress.models.User;
import com.daiken.workoutprogress.models.Workout;
import com.daiken.workoutprogress.repositories.ExerciseLogRepository;
import com.daiken.workoutprogress.repositories.ExerciseRepository;
import com.daiken.workoutprogress.repositories.WorkoutRepository;
import com.daiken.workoutprogress.services.UserService;
import com.daiken.workoutprogress.services.WorkoutService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@PreAuthorize("isAuthenticated()")
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

    public Workout addExerciseLog(String workoutId, ExerciseLogInput input, Boolean autoAdjust) {
        User me = userService.getContextUser();
        if (me == null) {
            throw new NullPointerException("Me not found!");
        }

        Workout currentWorkout = workoutRepository.findById(workoutId).orElseThrow(() -> new NullPointerException("Workout not found with given id"));
        Exercise exercise = exerciseRepository.findById(input.exerciseId()).orElseThrow(() -> new NullPointerException("Exercise not found with given id"));
        exerciseLogRepository.save(new ExerciseLog(input, me, currentWorkout, exercise));


        if (autoAdjust) {
            workoutService.adjustWorkoutMuscleGroups(currentWorkout);
        }

        return currentWorkout;
    }

    public Workout updateExerciseLog(String exerciseLogId, ExerciseLogInput input) {
        User me = userService.getContextUser();
        if (me == null) {
            throw new NullPointerException("Me not found!");
        }
        ExerciseLog exerciseLog = exerciseLogRepository.findById(exerciseLogId).orElseThrow(() -> new NullPointerException("Can't find exerciselog with given id"));
        exerciseLog.update(input);
        exerciseLogRepository.save(exerciseLog);
        return workoutRepository.findById(exerciseLog.getWorkout().getId()).orElse(null);
    }

    public Boolean removeExerciseLog(String exerciseLogId, boolean autoAdjust) {
        ExerciseLog exerciseLog = exerciseLogRepository.findById(exerciseLogId).orElseThrow(() -> new NullPointerException("ExerciseLog not found with given id"));
        exerciseLogRepository.delete(exerciseLog);
        if (autoAdjust) {
            workoutService.adjustWorkoutMuscleGroups(exerciseLog.getWorkout().getId());
        }
        return true;
    }

    public Workout reLogLatestLog(String workoutId, String zonedDateTimeString, Boolean autoAdjust) {
        User me = userService.getContextUser();
        if (me == null) {
            throw new NullPointerException("Me not found!");
        }

        Workout currentWorkout = workoutRepository.findById(workoutId).orElseThrow(() -> new NullPointerException("Workout not found with given id"));
        ExerciseLog exerciseLog = exerciseLogRepository.findLastLogByUserIdAndWorkoutId(me.getId(), workoutId)
                .orElseThrow(() -> new NullPointerException("No log for given workout id!"));
        exerciseLogRepository.save(new ExerciseLog(exerciseLog, zonedDateTimeString));

        if (autoAdjust) {
            workoutService.adjustWorkoutMuscleGroups(currentWorkout);
        }

        return currentWorkout;
    }

    public Workout reLogLog(String workoutId, ExerciseLogInput input) {
        User me = userService.getContextUser();
        if (me == null) {
            throw new NullPointerException("Me not found!");
        }

        Workout currentWorkout = workoutRepository.findById(workoutId).orElseThrow(() -> new NullPointerException("Workout not found with given id"));
        Exercise exercise = exerciseRepository.findById(input.exerciseId()).orElseThrow(() -> new NullPointerException("Exercise not found with given id"));
        exerciseLogRepository.save(new ExerciseLog(input, me, currentWorkout, exercise));

        return currentWorkout;
    }

}
