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

/**
 * Query resolver for ExerciseLog
 */
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

    /**
     * Get latest exercise logs by exercise id
     *
     * @param exerciseLogId exercise log id
     * @return list of exercise logs or null if workout not found
     */
    public List<ExerciseLog> latestLogsByExerciseId(String exerciseLogId) {
        User me = userService.getContextUser();
        ExerciseLog latestLoggedExercise = exerciseLogRepository.findLastLogByUserIdAndExerciseId(me.getId(), exerciseLogId).orElse(null);
        return getExerciseLogsByExerciseId(me.getId(), exerciseLogId, latestLoggedExercise);
    }

    /**
     * Get latest exercise logs by exercise id and workout id
     * @param exerciseLogId exercise log id
     * @param workoutId workout id
     * @return list of exercise logs or null if workout not found
     */
    public List<ExerciseLog> latestLogsByExerciseIdAndNotWorkoutId(String exerciseLogId, String workoutId) {
        User me = userService.getContextUser();
        Workout workout = workoutRepository.findWorkoutByIdAndUserId(workoutId, me.getId()).orElse(null);
        if (workout == null) {
            log.error("[latestLogsByExerciseIdAndNotWorkoutId] Workout with id {} not found", workoutId);
            return null;
        }
        ExerciseLog latestLoggedExercise = exerciseLogRepository.findFirstByUserIdAndExerciseIdAndWorkoutNotOrderByLogDateTimeDesc(me.getId(), exerciseLogId, workout).orElse(null);
        return getExerciseLogsByExerciseId(me.getId(), exerciseLogId, latestLoggedExercise);
    }

    /**
     * Get all exercise logs by exercise id
     * @param exerciseId exercise id
     * @return list of exercise logs
     */
    public List<ExerciseLog> allLogsByExerciseId(String exerciseId) {
        User me = userService.getContextUser();
        return exerciseLogRepository.findAllByUserIdAndExerciseIdOrderByLogDateTimeDesc(me.getId(), exerciseId);
    }

    /**
     * Get all exercise logs by exercise id between given zoned date time and time minus x months
     * @param exerciseId exercise id
     * @param months number of months to subtract (to)
     * @param zonedDateTimeString zoned date time string (from)
     * @return list of exercise logs between given zoned date time and time minus x months
     */
    public List<ExerciseLineChartData> chartDataOfXMonthsByExerciseId(String exerciseId, int months, String zonedDateTimeString) {
        User me = userService.getContextUser();
        LocalDateTime from = LocalDateTime.now().minusMonths(months);
        List<ExerciseLog> exerciseLogs = exerciseLogRepository.findAllByUserIdAndExerciseIdAndLogDateTimeBetween(me.getId(), exerciseId, from, ZonedDateTime.parse(zonedDateTimeString).toLocalDateTime());

        return exerciseLogService.mapLogsToChartData(exerciseLogs);
    }

    /**
     * Get all exercise logs by exercise id and workout id associated with the given last logged exercise
     * @param meId user id
     * @param exerciseLogId exercise log id
     * @param latestLoggedExercise last logged exercise
     * @return list of exercise logs
     */
    private List<ExerciseLog> getExerciseLogsByExerciseId(String meId, String exerciseLogId, ExerciseLog latestLoggedExercise) {
        if (latestLoggedExercise == null || latestLoggedExercise.getWorkout().getId() == null) {
            log.error("[getExerciseLogsByExerciseId] ExerciseLog is null or workout id is null. {}", latestLoggedExercise);
            return null;
        }

        return exerciseLogRepository
                .findLastLogsByUserIdWorkoutIdAndExerciseId(meId, latestLoggedExercise.getWorkout().getId(), exerciseLogId)
                .stream()
                .peek(it -> it.setExercise(exerciseRepository.findExerciseByIdAndUserId(it.getExercise().getId(), meId).orElse(null)))
                .filter(it -> it.getExercise() != null)
                .collect(Collectors.toList());
    }

}
