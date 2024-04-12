package com.daiken.workoutprogress.api.graphql;

import com.daiken.workoutprogress.models.ExerciseLineChartData;
import com.daiken.workoutprogress.models.ExerciseLog;
import com.daiken.workoutprogress.models.User;
import com.daiken.workoutprogress.models.Workout;
import com.daiken.workoutprogress.repositories.ExerciseLogRepository;
import com.daiken.workoutprogress.repositories.ExerciseRepository;
import com.daiken.workoutprogress.repositories.WorkoutRepository;
import com.daiken.workoutprogress.services.ExerciseLogService;
import com.daiken.workoutprogress.services.UserService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@PreAuthorize("isAuthenticated()")
@Service
public class ExerciseLogQueryResolver implements GraphQLQueryResolver {

    private final ExerciseLogRepository exerciseLogRepository;
    private final ExerciseLogService exerciseLogService;
    private final ExerciseRepository exerciseRepository;
    private final UserService userService;
    private final WorkoutRepository workoutRepository;

    @Autowired
    public ExerciseLogQueryResolver(ExerciseLogRepository exerciseLogRepository,
                                    ExerciseLogService exerciseLogService,
                                    ExerciseRepository exerciseRepository,
                                    UserService userService,
                                    WorkoutRepository workoutRepository) {
        this.exerciseLogRepository = exerciseLogRepository;
        this.exerciseLogService = exerciseLogService;
        this.exerciseRepository = exerciseRepository;
        this.userService = userService;
        this.workoutRepository = workoutRepository;
    }

    public List<ExerciseLog> latestLogsByExerciseId(String exerciseLogId) {
        User me = userService.getContextUser();
        ExerciseLog latestLoggedExercise = exerciseLogRepository.findLastLogByUserIdAndExerciseId(me.getId(), exerciseLogId).orElse(null);
        return getExerciseLogsByExerciseId(exerciseLogId, latestLoggedExercise);
    }

    public List<ExerciseLog> latestLogsByExerciseIdAndNotWorkoutId(String exerciseLogId, String workoutId) {
        User me = userService.getContextUser();
        Workout workout = workoutRepository.findById(workoutId).orElse(null);
        if (workout == null) {
            log.error("[latestLogsByExerciseIdAndNotWorkoutId] Workout with id {} not found", workoutId);
            return null;
        }
        ExerciseLog latestLoggedExercise = exerciseLogRepository.findFirstByUserIdAndExerciseIdAndWorkoutNotOrderByLogDateTimeDesc(me.getId(), exerciseLogId, workout).orElse(null);
        return getExerciseLogsByExerciseId(exerciseLogId, latestLoggedExercise);
    }

    public List<ExerciseLog> allLogsByExerciseId(String exerciseId) {
        User me = userService.getContextUser();
        return exerciseLogRepository.findAllByUserIdAndExerciseIdOrderByLogDateTimeDesc(me.getId(), exerciseId);
    }

    public List<ExerciseLineChartData> chartDataOfXMonthsByExerciseId(String exerciseId, int months, String zonedDateTimeString) {
        User me = userService.getContextUser();
        LocalDateTime from = LocalDateTime.now().minusMonths(months);
        List<ExerciseLog> exerciseLogs = exerciseLogRepository.findAllByUserIdAndExerciseIdAndLogDateTimeBetween(me.getId(), exerciseId, from, ZonedDateTime.parse(zonedDateTimeString).toLocalDateTime());

        return exerciseLogService.mapLogsToChartData(exerciseLogs);
    }

    private List<ExerciseLog> getExerciseLogsByExerciseId(String exerciseLogId, ExerciseLog latestLoggedExercise) {
        if (latestLoggedExercise == null || latestLoggedExercise.getWorkout().getId() == null) {
            log.error("[getExerciseLogsByExerciseId] ExerciseLog is null or workout id is null. {}", latestLoggedExercise);
            return null;
        }

        return exerciseLogRepository
                .findLastLogsByWorkoutIdAndExerciseId(latestLoggedExercise.getWorkout().getId(), exerciseLogId)
                .stream()
                .peek(it -> it.setExercise(exerciseRepository.findById(it.getExercise().getId()).orElse(null)))
                .filter(it -> it.getExercise() != null)
                .collect(Collectors.toList());
    }

}
