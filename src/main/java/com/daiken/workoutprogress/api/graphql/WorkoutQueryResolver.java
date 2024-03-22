package com.daiken.workoutprogress.api.graphql;

import com.daiken.workoutprogress.models.MuscleGroup;
import com.daiken.workoutprogress.models.MuscleGroupChartData;
import com.daiken.workoutprogress.models.User;
import com.daiken.workoutprogress.models.Workout;
import com.daiken.workoutprogress.repositories.WorkoutRepository;
import com.daiken.workoutprogress.services.UserService;
import com.daiken.workoutprogress.services.WorkoutService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

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

    public List<Workout> myWorkouts() {
        User me = userService.getContextUser();
        List<Workout> workouts = workoutRepository.findWorkoutByUserId(me.getId());
        Collections.sort(workouts);
        return workouts;
    }

    public Boolean meHasActiveWorkout() {
        User me = userService.getContextUser();
        long workouts = workoutRepository.countWorkoutsByUserAndActive(me, true);
        return workouts > 0;
    }

    public Workout workoutById(String id) {
        return workoutRepository.findById(id).orElse(null);
    }

    public List<Workout> workoutsOfCurrentMonth(String zonedDateTimeString) {
        User me = userService.getContextUser();
        LocalDateTime localDateTime = ZonedDateTime.parse(zonedDateTimeString).toLocalDateTime();
        return workoutRepository.findByUserIdEndDateTimeYearAndMonth(me.getId(), localDateTime.withDayOfMonth(1).toLocalDate(), localDateTime.withDayOfMonth(1).plusMonths(1).toLocalDate());
    }

    public long countTotalTimeAllMyWorkoutsInMinutes() {
        return myWorkouts().stream()
                .filter(workout -> !workout.isActive())
                .mapToLong(workout -> Duration.between(workout.getStartDateTime(), workout.getEndDateTime()).toMinutes())
                .sum();

    }

    public int countMyWorkouts() {
        User me = userService.getContextUser();
        return workoutRepository.countWorkoutsByUserId(me.getId());
    }

    public List<MuscleGroupChartData> chartDataMuscleGroups() {
        List<MuscleGroup> muscleGroupsPerWorkout = myWorkouts()
                .stream()
                .flatMap(workout -> workout.getMuscleGroups().stream()).toList();
        List<MuscleGroupChartData> muscleGroupChartData = workoutService.mapMuscleGroupsToChartData(muscleGroupsPerWorkout);
        Collections.sort(muscleGroupChartData);
        return muscleGroupChartData;
    }
}
