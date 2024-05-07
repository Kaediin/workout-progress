package com.daiken.workoutprogress.migrations;

import com.daiken.workoutprogress.annotation.Migratable;
import com.daiken.workoutprogress.annotation.Migration;
import com.daiken.workoutprogress.models.WorkoutStatus;
import com.daiken.workoutprogress.repositories.WorkoutRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Migrations for Workout
 */
@Slf4j
@Migratable
public class WorkoutMigrations {

    public static final int LAST_MIGRATION_ORDER = 5;

    @Migration(key = "setWorkoutStatus", order = 5, author = "Kaedin")
    public void setWorkoutStatus(List<String> messages, WorkoutRepository workoutRepository) {
        messages.add("[WorkoutMigrations] setWorkoutStatus Setting workout status");
        workoutRepository.saveAll(
                workoutRepository.findAll()
                        .stream()
                        .peek(it -> it.setStatus(it.getEndDateTime() != null ? WorkoutStatus.ENDED : WorkoutStatus.STARTED))
                        .collect(Collectors.toList())

        );
    }

}
