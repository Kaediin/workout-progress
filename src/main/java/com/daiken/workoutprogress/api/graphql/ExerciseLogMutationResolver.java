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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * Mutation resolver for exercise logs
 */
@Slf4j
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

    /**
     * Add exercise log
     *
     * @param workoutId  workout id
     * @param input      exercise log input
     * @param autoAdjust auto adjust muscle groups in workout
     * @return workout with added exercise log
     */
    public Workout addExerciseLog(String workoutId, ExerciseLogInput input, Boolean autoAdjust) {
        User me = userService.getContextUser();
        Workout currentWorkout = workoutRepository.findWorkoutByIdAndUserId(workoutId, me.getId()).orElseThrow(() -> {
            log.error("[addExerciseLog] Workout not found with given id: {}", workoutId);
            return new NullPointerException("Workout not found with given id");
        });
        Exercise exercise = exerciseRepository.findExerciseByIdAndUserId(input.exerciseId(), me.getId()).orElseThrow(() -> {
            log.error("[addExerciseLog] Exercise not found with given id: {}", input.exerciseId());
            return new NullPointerException("Exercise not found with given id");
        });
        exerciseLogRepository.save(new ExerciseLog(input, me, currentWorkout, exercise));


        if (autoAdjust) {
            workoutService.adjustWorkoutMuscleGroups(currentWorkout, me);
        }

        return currentWorkout;
    }

    /**
     * Update exercise log
     * @param exerciseLogId exercise log id
     * @param input exercise log input
     * @return workout with updated exercise log
     */
    public Workout updateExerciseLog(String exerciseLogId, ExerciseLogInput input) {
        User me = userService.getContextUser();

        ExerciseLog exerciseLog = exerciseLogRepository.findByIdAndUserId(exerciseLogId, me.getId()).orElseThrow(() -> {
            log.error("[updateExerciseLog] Can't find exerciselog with given id: {}", exerciseLogId);
            return new NullPointerException("Can't find exerciselog with given id");
        });

        exerciseLog.update(input);
        exerciseLogRepository.save(exerciseLog);
        return workoutRepository.findWorkoutByIdAndUserId(exerciseLog.getWorkout().getId(), me.getId()).orElse(null);
    }

    /**
     * Remove exercise log
     * @param exerciseLogId exercise log id
     * @param autoAdjust auto adjust muscle groups in workout
     * @return true if removed successfully
     */
    public Boolean removeExerciseLog(String exerciseLogId, boolean autoAdjust) {
        User me = userService.getContextUser();
        ExerciseLog exerciseLog = exerciseLogRepository.findByIdAndUserId(exerciseLogId, me.getId()).orElseThrow(() -> {
            log.error("[removeExerciseLog] ExerciseLog not found with given id: {}", exerciseLogId);
            return new NullPointerException("ExerciseLog not found with given id");
        });
        exerciseLogRepository.delete(exerciseLog);
        if (autoAdjust) {
            workoutService.adjustWorkoutMuscleGroups(exerciseLog.getWorkout().getId(), me);
        }
        return true;
    }

    /**
     * Re-log latest log
     * @param workoutId workout id
     * @param zonedDateTimeString zoned date time string
     * @param autoAdjust auto adjust muscle groups in workout
     * @return workout with re-logged latest log
     */
    public Workout reLogLatestLog(String workoutId, String zonedDateTimeString, Boolean autoAdjust) {
        User me = userService.getContextUser();

        Workout currentWorkout = workoutRepository.findWorkoutByIdAndUserId(workoutId, me.getId()).orElseThrow(() -> {
            log.error("[reLogLatestLog] Workout not found with given id: {}", workoutId);
            return new NullPointerException("Workout not found with given id");
        });
        ExerciseLog exerciseLog = exerciseLogRepository.findLastLogByUserIdAndWorkoutId(me.getId(), workoutId)
                .orElseThrow(() -> {
                    log.error("[reLogLatestLog] No log for given workout id: {}", workoutId);
                    return new NullPointerException("No log for given workout id!");
                });
        exerciseLogRepository.save(new ExerciseLog(exerciseLog, zonedDateTimeString));

        if (autoAdjust) {
            workoutService.adjustWorkoutMuscleGroups(currentWorkout, me);
        }

        return currentWorkout;
    }

    /**
     * Re-log any log
     * @param workoutId workout id
     * @param input exercise log input
     * @return workout with re-logged log
     */
    public Workout reLogLog(String workoutId, ExerciseLogInput input) {
        User me = userService.getContextUser();

        Workout currentWorkout = workoutRepository.findWorkoutByIdAndUserId(workoutId, me.getId()).orElseThrow(() -> {
            log.error("[reLogLog] Workout not found with given id: {}", workoutId);
            return new NullPointerException("Workout not found with given id");
        });
        Exercise exercise = exerciseRepository.findExerciseByIdAndUserId(input.exerciseId(), me.getId()).orElseThrow(() -> {
            log.error("[reLogLog] Exercise not found with given id: {}", input.exerciseId());
            return new NullPointerException("Exercise not found with given id");
        });
        exerciseLogRepository.save(new ExerciseLog(input, me, currentWorkout, exercise));

        return currentWorkout;
    }

}
