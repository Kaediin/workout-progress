package com.daiken.workoutprogress.api.graphql;

import com.daiken.workoutprogress.models.*;
import com.daiken.workoutprogress.repositories.WorkoutRepository;
import com.daiken.workoutprogress.services.UserService;
import com.daiken.workoutprogress.services.WorkoutService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

/**
 * Query resolver for Workout
 */
@Slf4j
@Component
@PreAuthorize("isAuthenticated()")
public class WorkoutQueryResolver implements GraphQLQueryResolver {

    private final UserService userService;
    private final WorkoutRepository workoutRepository;
    private final WorkoutService workoutService;

    @Autowired
    public WorkoutQueryResolver(
            UserService userService,
            WorkoutRepository workoutRepository,
            WorkoutService workoutService
    ) {
        this.userService = userService;
        this.workoutRepository = workoutRepository;
        this.workoutService = workoutService;
    }

    /**
     * Get all users workouts
     *
     * @return List of users workouts
     */
    public List<Workout> myWorkouts() {
        User me = userService.getContextUser();
        List<Workout> workouts = workoutRepository.findWorkoutByUserIdAndProgramIsNull(me.getId());
        Collections.sort(workouts);
        return workouts;
    }

    /**
     * Check if current user has an active workout
     * @return True if user has an active workout
     */
    public Boolean meHasActiveWorkout() {
        User me = userService.getContextUser();
        long workouts = workoutRepository.countWorkoutsByUserAndStatus(me, WorkoutStatus.STARTED);
        return workouts > 0;
    }

    /**
     * Get workout by id
     * @param id Workout id
     * @return Workout
     */
    public Workout workoutById(String id) {
        User me = userService.getContextUser();
        return workoutRepository.findWorkoutByIdAndUserId(id, me.getId()).orElse(null);
    }

    /**
     * Get all workouts of current month
     * @param zonedDateTimeString Zoned date time string
     * @return List of workouts of current month
     */
    public List<Workout> workoutsOfCurrentMonth(String zonedDateTimeString) {
        User me = userService.getContextUser();
        LocalDateTime localDateTime = ZonedDateTime.parse(zonedDateTimeString).toLocalDateTime();
        return workoutRepository.findByUserIdEndDateTimeYearAndMonth(me.getId(), localDateTime.withDayOfMonth(1).toLocalDate(), localDateTime.withDayOfMonth(1).plusMonths(1).toLocalDate());
    }

    /**
     * Count total time of all my workouts in minutes
     * @return Total time of all my workouts in minutes
     */
    public long countTotalTimeAllMyWorkoutsInMinutes() {
        return myWorkouts().stream()
                .filter(workout -> !workout.isActive())
                .mapToLong(workout -> Duration.between(workout.getStartDateTime(), workout.getEndDateTime()).toMinutes())
                .sum();

    }

    /**
     * Count my workouts
     * @return Total workouts of current user
     */
    public int countMyWorkouts() {
        User me = userService.getContextUser();
        return workoutRepository.countWorkoutsByUserId(me.getId());
    }

    /**
     * Get my workout chart data
     * @return List of workout chart data
     */
    public List<MuscleGroupChartData> chartDataMuscleGroups(String zonedDateTimeString) {
        List<MuscleGroup> muscleGroupsPerWorkout = (zonedDateTimeString != null ? workoutsOfCurrentMonth(zonedDateTimeString) : myWorkouts())
                .stream()
                .flatMap(workout -> workout.getMuscleGroups().stream()).toList();
        List<MuscleGroupChartData> muscleGroupChartData = workoutService.mapMuscleGroupsToChartData(muscleGroupsPerWorkout);
        Collections.sort(muscleGroupChartData);
        return muscleGroupChartData;
    }
}
