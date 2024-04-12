package com.daiken.workoutprogress.api.graphql;

import com.daiken.workoutprogress.api.graphql.input.ExternalHealthProviderData;
import com.daiken.workoutprogress.api.graphql.input.WorkoutInput;
import com.daiken.workoutprogress.models.ExerciseLog;
import com.daiken.workoutprogress.models.User;
import com.daiken.workoutprogress.models.Workout;
import com.daiken.workoutprogress.repositories.ExerciseLogRepository;
import com.daiken.workoutprogress.repositories.WorkoutRepository;
import com.daiken.workoutprogress.services.UserService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Mutation resolver for Workout
 */
@Slf4j
@PreAuthorize("isAuthenticated()")
@Component
public class WorkoutMutationResolver implements GraphQLMutationResolver {

    private final ExerciseLogRepository exerciseLogRepository;
    private final UserService userService;
    private final WorkoutRepository workoutRepository;

    @Autowired
    public WorkoutMutationResolver(
            ExerciseLogRepository exerciseLogRepository,
            UserService userService,
            WorkoutRepository workoutRepository
    ) {
        this.exerciseLogRepository = exerciseLogRepository;
        this.userService = userService;
        this.workoutRepository = workoutRepository;
    }

    /**
     * Start a workout
     *
     * @param input Workout input
     * @return Started workout
     */
    public Workout meStartWorkout(WorkoutInput input) {
        if (input.name() == null) {
            log.error("[meStartWorkout] Input is not filled properly! {}", input);
            throw new NullPointerException("Input is not filled properly!");
        }
        User user = userService.getContextUser();
        Workout workout = new Workout(input, userService.getContextUser(), true);

        // Deactivate the active workout if there is any
        Workout activeWorkout = workoutRepository.findWorkoutByUserIdAndActive(user.getId(), true).orElse(null);
        if (activeWorkout != null) {
            activeWorkout.setActive(false);
            workoutRepository.save(activeWorkout);
        }

        return workoutRepository.save(workout);
    }

    /**
     * End a workout with a end time of last log if there is any, otherwise end workout with given time
     * @param workoutId Workout id
     * @param zonedDateTimeString Zoned date time string
     * @return Ended workout
     */
    public Workout endWorkout(String workoutId, String zonedDateTimeString) {
        User me = userService.getContextUser();

        Workout workout = workoutRepository.findWorkoutByIdAndUserId(workoutId, me.getId()).orElseThrow(() -> {
            log.error("[endWorkout] Workout not found with given id {}", workoutId);
            return new NullPointerException("Workout not found with given id");
        });

        // End workout with time of last log if there is any, otherwise end workout with given time
        Optional<ExerciseLog> exerciseLog = exerciseLogRepository.findLastLogByUserIdAndWorkoutId(me.getId(), workoutId);
        if (exerciseLog.isPresent() && exerciseLog.get().getLogDateTime() != null) {
            workout.endWorkout(exerciseLog.get().getLogDateTime());
        } else {
            workout.endWorkout(ZonedDateTime.parse(zonedDateTimeString).toLocalDateTime());
        }
        return workoutRepository.save(workout);
    }

    /**
     * Delete a workout
     * @param id Workout id
     * @return True if successful
     */
    public Boolean deleteWorkout(String id) {
        User me = userService.getContextUser();
        Workout workout = workoutRepository.findWorkoutByIdAndUserId(id, me.getId()).orElseThrow(() -> {
            log.error("[deleteWorkout] Cant find workout with given id {}", id);
            return new NullPointerException("Cant find workout with given id");
        });
        List<ExerciseLog> logs = exerciseLogRepository.findAllByWorkoutIdAndUserId(workout.getId(), me.getId());
        exerciseLogRepository.deleteAll(logs);
        workoutRepository.delete(workout);
        return true;
    }

    /**
     * Update a workout
     * @param id Workout id
     * @param input Workout input
     * @return Updated workout
     */
    public Workout updateWorkout(String id, WorkoutInput input) {
        User me = userService.getContextUser();
        Workout workout = workoutRepository.findWorkoutByIdAndUserId(id, me.getId()).orElseThrow(() -> {
            log.error("[updateWorkout] Cant find workout with given id {}", id);
            return new NullPointerException("Cant find workout with given id");
        });
        return workoutRepository.save(workout.update(input));
    }

    /**
     * Restart a workout
     * @param id Workout id
     * @return Restarted workout
     */
    public Workout restartWorkout(String id) {
        User me = userService.getContextUser();
        Workout workout = workoutRepository.findWorkoutByIdAndUserId(id, me.getId()).orElseThrow(() -> {
            log.error("[restartWorkout] Cant find workout with given id {}", id);
            return new NullPointerException("Cant find workout with given id");
        });
        workout.setActive(true);
        workout.setEndDateTime(null);
        return workoutRepository.save(workout);
    }

    /**
     * Add external health provider data
     * @param workoutId workout id
     * @param providerData external health provider data
     * @return Updated workout
     */
    public Workout addExternalHealthProviderData(String workoutId, ExternalHealthProviderData providerData) {
        User me = userService.getContextUser();
        Workout workout = workoutRepository.findWorkoutByIdAndUserId(workoutId, me.getId()).orElseThrow(() -> {
            log.error("[addExternalHealthProviderData] Cant find workout with given id {}", workoutId);
            return new NullPointerException("Cant find workout with given id");
        });
        workout.setExternalHealthProviderData(providerData);
        return workoutRepository.save(workout);
    }

    /**
     * Add estimated calories burned
     * @param workoutId workout id
     * @param estimatedCaloriesBurned estimated calories burned
     * @return Updated workout
     */
    public Workout addEstimatedCaloriesBurned(String workoutId, Long estimatedCaloriesBurned) {
        User me = userService.getContextUser();
        Workout workout = workoutRepository.findWorkoutByIdAndUserId(workoutId, me.getId()).orElseThrow(() -> {
            log.error("[addEstimatedCaloriesBurned] Cant find workout with given id {}", workoutId);
            return new NullPointerException("Cant find workout with given id");
        });
        workout.setEstimatedCaloriesBurned(estimatedCaloriesBurned);
        return workoutRepository.save(workout);
    }
}
