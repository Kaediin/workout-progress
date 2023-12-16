package com.daiken.workoutprogress.api.graphql;

import com.daiken.workoutprogress.api.graphql.input.WorkoutInput;
import com.daiken.workoutprogress.model.ExerciseLog;
import com.daiken.workoutprogress.model.User;
import com.daiken.workoutprogress.model.Workout;
import com.daiken.workoutprogress.repository.ExerciseLogRepository;
import com.daiken.workoutprogress.repository.ExerciseRepository;
import com.daiken.workoutprogress.repository.WorkoutRepository;
import com.daiken.workoutprogress.services.UserService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class WorkoutMutationResolver implements GraphQLMutationResolver {

    private final ExerciseLogRepository exerciseLogRepository;
    private final ExerciseRepository exerciseRepository;
    private final UserService userService;
    private final WorkoutRepository workoutRepository;

    @Autowired
    public WorkoutMutationResolver(
            ExerciseLogRepository exerciseLogRepository,
            ExerciseRepository exerciseRepository,
            UserService userService,
            WorkoutRepository workoutRepository
    ) {
        this.exerciseLogRepository = exerciseLogRepository;
        this.exerciseRepository = exerciseRepository;
        this.userService = userService;
        this.workoutRepository = workoutRepository;
    }

    @PreAuthorize("isAuthenticated()")
    public Workout meStartWorkout(WorkoutInput input) {
        if (input.getName() == null) {
            throw new NullPointerException("Input is not filled properly!");
        }
        User user = userService.getContextUser();
        Workout workout = new Workout(input, userService.getContextUser(), true);

        Workout activeWorkout = workoutRepository.findWorkoutByUserIdAndActive(user.getId(), true).orElse(null);
        if (activeWorkout != null) {
            activeWorkout.active = false;
            workoutRepository.save(activeWorkout);
        }

        return workoutRepository.save(workout);
    }

    @PreAuthorize("isAuthenticated()")
    public Workout endWorkout(String workoutId, String zonedDateTimeString) {
        User me = userService.getContextUser();
        if (me == null) {
            throw new NullPointerException("Me not found!");
        }
        Workout workout = workoutRepository.findById(workoutId).orElseThrow(() -> new NullPointerException("Workout not found with given id"));
        Optional<ExerciseLog> exerciseLog = exerciseLogRepository.findLastLogByUserIdAndWorkoutId(me.getId(), workoutId);
        if (exerciseLog.isPresent() && exerciseLog.get().logDateTime != null) {
            workout.endWorkout(exerciseLog.get().logDateTime);
        } else {
            workout.endWorkout(ZonedDateTime.parse(zonedDateTimeString).toLocalDateTime());
        }
        return workoutRepository.save(workout);
    }

    @PreAuthorize("isAuthenticated()")
    public Boolean deleteWorkout(String id) {
        Workout workout = workoutRepository.findById(id).orElseThrow(() -> new NullPointerException("Cant find workout with given id"));
        List<ExerciseLog> logs = exerciseLogRepository.findAllByWorkoutId(workout.id);
        exerciseLogRepository.deleteAll(logs);
        workoutRepository.delete(workout);
        return true;
    }

    @PreAuthorize("isAuthenticated()")
    public Workout updateWorkout(String id, WorkoutInput input) {
        Workout workout = workoutRepository.findById(id).orElseThrow(() -> new NullPointerException("Cant find workout with given id"));
        return workoutRepository.save(workout.update(input));
    }
}
