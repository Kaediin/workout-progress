package com.daiken.workoutprogress.migrations;

import com.daiken.workoutprogress.annotation.Migratable;
import com.daiken.workoutprogress.annotation.Migration;
import com.daiken.workoutprogress.models.Workout;
import com.daiken.workoutprogress.repositories.WorkoutRepository;
import com.daiken.workoutprogress.services.WorkoutService;

import java.util.List;

@Migratable
public class WorkoutMigrations {

    @Migration(key = "updateWorkoutMuscleGroups", order = 4, author = "Kaedin")
    public void weightLogToWeightValue(List<String> messages, WorkoutRepository workoutRepository, WorkoutService workoutService) {
        // This auto adjusts all the workouts
        List<Workout> workouts = workoutRepository.findAll();
        workouts.forEach(workoutService::adjustWorkoutMuscleGroups);
    }
}
