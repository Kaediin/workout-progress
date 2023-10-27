package com.daiken.workoutprogress.services;

import com.daiken.workoutprogress.model.Exercise;
import com.daiken.workoutprogress.model.MuscleGroup;
import com.daiken.workoutprogress.model.Workout;
import com.daiken.workoutprogress.repository.ExerciseLogRepository;
import com.daiken.workoutprogress.repository.ExerciseRepository;
import com.daiken.workoutprogress.repository.WorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
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
        Set<MuscleGroup> groupsBasedOnPrimary = exerciseLogRepository
                .findAllByWorkoutId(workout.id)
                .stream()
                .map(it -> exerciseRepository.findById(it.exercise.id).orElse(null))
                .filter(Objects::nonNull)
                .flatMap(it -> it.primaryMuscles.stream())
                .collect(Collectors.toSet());

        Set<MuscleGroup> groupsBasedOnSecondary = exerciseLogRepository
                .findAllByWorkoutId(workout.id)
                .stream()
                .map(it -> exerciseRepository.findById(it.exercise.id).orElse(null))
                .filter(Objects::nonNull)
                .filter(distinctByKey(Exercise::getId))
                .flatMap(it -> it.secondaryMuscles.stream())
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
