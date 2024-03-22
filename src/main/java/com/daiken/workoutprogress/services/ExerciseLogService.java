package com.daiken.workoutprogress.services;

import com.daiken.workoutprogress.models.ExerciseLineChartData;
import com.daiken.workoutprogress.models.ExerciseLog;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExerciseLogService {

    public ExerciseLogService() {
    }

    public List<ExerciseLineChartData> mapLogsToChartData(List<ExerciseLog> logs) {
        Map<Month, List<ExerciseLog>> logMap = logs
                .stream()
                .collect(Collectors.groupingBy(log -> log.getLogDateTime().getMonth()));

        return logMap.entrySet().stream()
                .map(entry -> new ExerciseLineChartData(entry.getKey().name(), entry.getValue()))
                .collect(Collectors.toList());
    }
}
