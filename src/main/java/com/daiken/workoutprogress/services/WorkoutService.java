package com.daiken.workoutprogress.services;

import com.daiken.workoutprogress.models.Exercise;
import com.daiken.workoutprogress.models.MuscleGroup;
import com.daiken.workoutprogress.models.Workout;
import com.daiken.workoutprogress.repositories.ExerciseLogRepository;
import com.daiken.workoutprogress.repositories.ExerciseRepository;
import com.daiken.workoutprogress.repositories.WorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
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

    public void adjustWorkoutMuscleGroups(String workoutId) {
        adjustWorkoutMuscleGroups(workoutRepository.findById(workoutId).orElseThrow(() -> new NullPointerException("Cant find workout with given id")));
    }

    public void adjustWorkoutMuscleGroups(Workout workout) {
        List<Exercise> exercisesDoneThisWorkout = exerciseLogRepository.findAllByWorkoutId(workout.id)
                .stream()
                .map(it -> exerciseRepository.findById(it.exercise.id).orElse(null))
                .filter(Objects::nonNull)
                .toList();

        Set<MuscleGroup> groupsBasedOnPrimary = exercisesDoneThisWorkout
                .stream()
                .filter(it -> it.getPrimaryMuscles() != null)
                .flatMap(it -> it.getPrimaryMuscles().stream())
                .collect(Collectors.toSet());

        Set<MuscleGroup> groupsBasedOnSecondary = exercisesDoneThisWorkout
                .stream()
                .filter(it -> it.getSecondaryMuscles() != null)
                .filter(distinctByKey(it -> it.id))
                .flatMap(it -> it.getSecondaryMuscles().stream())
                .collect(Collectors.groupingBy(i -> i, Collectors.counting()))
                .entrySet().stream()
                .filter(i -> i.getValue() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        groupsBasedOnPrimary.addAll(groupsBasedOnSecondary);

        workout.muscleGroups = groupsBasedOnPrimary
                .stream()
                .sorted(Comparator.comparing(MuscleGroup::name))
                .collect(Collectors.toList());
        workoutRepository.save(workout);
    }

    public static <T> java.util.function.Predicate<T> distinctByKey(java.util.function.Function<? super T, ?> keyExtractor) {
        java.util.Map<Object, Boolean> seen = new java.util.concurrent.ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
