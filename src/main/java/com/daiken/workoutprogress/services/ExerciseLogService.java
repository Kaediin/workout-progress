package com.daiken.workoutprogress.services;

import com.daiken.workoutprogress.models.ExerciseLineChartData;
import com.daiken.workoutprogress.models.ExerciseLog;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service for ExerciseLog operations.
 */
@Service
public class ExerciseLogService {

    public ExerciseLogService() {
    }

    /**
     * Map a list of ExerciseLogs to a list of ExerciseLineChartData.
     *
     * @param logs List of ExerciseLogs
     * @return List of ExerciseLineChartData
     */
    public List<ExerciseLineChartData> mapLogsToChartData(List<ExerciseLog> logs) {
        Map<Month, List<ExerciseLog>> logMap = logs
                .stream()
                .collect(Collectors.groupingBy(log -> log.getLogDateTime().getMonth()));

        return logMap.entrySet().stream()
                .map(entry -> new ExerciseLineChartData(entry.getKey().name(), entry.getValue()))
                .collect(Collectors.toList());
    }
}
