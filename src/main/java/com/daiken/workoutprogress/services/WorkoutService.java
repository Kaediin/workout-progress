package com.daiken.workoutprogress.services;

import com.daiken.workoutprogress.model.Workout;
import com.daiken.workoutprogress.repository.ExerciseLogRepository;
import com.daiken.workoutprogress.repository.ExerciseRepository;
import com.daiken.workoutprogress.repository.WorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class WorkoutService {

    private final ExerciseLogRepository exerciseLogRepository;
    private final ExerciseRepository exerciseRepository;
    private final WorkoutRepository workoutRepository;

    @Autowired
    public WorkoutService(ExerciseLogRepository exerciseLogRepository,
                          ExerciseRepository exerciseRepository,
                          WorkoutRepository workoutRepository) {
        this.exerciseLogRepository = exerciseLogRepository;
        this.exerciseRepository = exerciseRepository;
        this.workoutRepository = workoutRepository;
    }

    public Workout adjustWorkoutMuscleGroups(String workoutId) {
        Workout workout = workoutRepository.findById(workoutId).orElseThrow(() -> new NullPointerException("Cant find workout with given id"));

        workout.muscleGroups = exerciseLogRepository
                .findAllByWorkoutId(workoutId)
                .stream()
                .map(it -> exerciseRepository.findById(it.exercise.id).orElse(null))
                .filter(Objects::nonNull)
                .flatMap(it -> it.primaryMuscles.stream())
                .collect(Collectors.toSet())
                .stream()
                .toList();
        return workoutRepository.save(workout);
    }
}
