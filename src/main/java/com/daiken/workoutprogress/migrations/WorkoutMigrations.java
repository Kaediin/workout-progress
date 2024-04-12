package com.daiken.workoutprogress.migrations;

import com.daiken.workoutprogress.annotation.Migratable;
import com.daiken.workoutprogress.annotation.Migration;
import com.daiken.workoutprogress.models.Workout;
import com.daiken.workoutprogress.repositories.WorkoutRepository;
import com.daiken.workoutprogress.services.WorkoutService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Migrations for Workout
 */
@Slf4j
@Migratable
public class WorkoutMigrations {

    /**
     * Update workout muscle groups
     *
     * @param messages          List of messages to add to
     * @param workoutRepository Workout repository
     * @param workoutService    Workout service
     */
    @Migration(key = "updateWorkoutMuscleGroups", order = 4, author = "Kaedin")
    public void weightLogToWeightValue(List<String> messages, WorkoutRepository workoutRepository, WorkoutService workoutService) {
        log.info("[weightLogToWeightValue] Started updating workout muscle groups");
        List<Workout> workouts = workoutRepository.findAll();
        workouts.forEach(workoutService::adjustWorkoutMuscleGroups);
        log.info("[weightLogToWeightValue] Finished updating {} workout muscle groups", workouts.size());
    }
}
