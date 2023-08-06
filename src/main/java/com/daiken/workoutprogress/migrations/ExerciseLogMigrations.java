package com.daiken.workoutprogress.migrations;

import com.daiken.workoutprogress.annotation.Migratable;
import com.daiken.workoutprogress.annotation.Migration;
import com.daiken.workoutprogress.model.ExerciseLog;
import com.daiken.workoutprogress.repository.ExerciseLogRepository;

import java.util.List;

@Migratable
public class ExerciseLogMigrations {

    @Migration(key = "weightLogToWeightValue", order = 1, author = "Kaedin")
    public void weightLogToWeightValue(List<String> messages, ExerciseLogRepository exerciseLogRepository) {
        List<ExerciseLog> logs = exerciseLogRepository
                .findAllByWeightLeftExistsOrWeightRightExists(true, true)
                .map(ExerciseLog::convertWeightToWeightValue)
                .toList();
        exerciseLogRepository.saveAll(logs);
    }

    @Migration(key = "weightLogToLogValue", order = 3, author = "Kaedin")
    public void weightLogToLogValue(List<String> messages, ExerciseLogRepository exerciseLogRepository) {
        List<ExerciseLog> logs = exerciseLogRepository
                .findAllByWeightLeftExistsOrWeightRightExists(true, true)
                .map(ExerciseLog::convertWeightToLogValue)
                .toList();
        exerciseLogRepository.saveAll(logs);
    }
}
