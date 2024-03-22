package com.daiken.workoutprogress.services;

import com.daiken.workoutprogress.models.Exercise;
import com.daiken.workoutprogress.models.MuscleGroup;
import com.daiken.workoutprogress.models.MuscleGroupChartData;
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
        List<Exercise> exercisesDoneThisWorkout = exerciseLogRepository.findAllByWorkoutId(workout.getId())
                .stream()
                .map(it -> exerciseRepository.findById(it.getExercise().getId()).orElse(null))
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
                .filter(distinctByKey(Exercise::getId))
                .flatMap(it -> it.getSecondaryMuscles().stream())
                .collect(Collectors.groupingBy(i -> i, Collectors.counting()))
                .entrySet().stream()
                .filter(i -> i.getValue() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        groupsBasedOnPrimary.addAll(groupsBasedOnSecondary);

        workout.setMuscleGroups(groupsBasedOnPrimary
                .stream()
                .sorted(Comparator.comparing(MuscleGroup::name))
                .collect(Collectors.toList()));
        workoutRepository.save(workout);
    }

    public static <T> java.util.function.Predicate<T> distinctByKey(java.util.function.Function<? super T, ?> keyExtractor) {
        java.util.Map<Object, Boolean> seen = new java.util.concurrent.ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    public List<MuscleGroupChartData> mapMuscleGroupsToChartData(List<MuscleGroup> muscleGroups) {
        Map<MuscleGroup, Long> muscleGroupCounter = muscleGroups
                .stream()
                .collect(Collectors.groupingBy(muscleGroup -> muscleGroup, Collectors.counting()));

        return muscleGroupCounter
                .entrySet()
                .stream()
                .map(entry -> new MuscleGroupChartData(entry.getKey(), mapMuscleGroupToColor(entry.getKey()), entry.getValue().intValue()))
                .collect(Collectors.toList());
    }

    private String mapMuscleGroupToColor(MuscleGroup muscleGroup) {
        return switch (muscleGroup) {
            case ABDUCTOR -> "#0a7ab2";
            case ABS -> "#336699";
            case ADDUCTOR -> "#3366cc";
            case BACK_SHOULDERS -> "#003399";
            case BICEPS -> "#3399ff";
            case CALVES -> "#4aa1a1";
            case CHEST -> "#5b99d7";
            case FOREARMS -> "#66ccff";
            case FRONT_SHOULDERS -> "#006699";
            case GLUTES -> "#0099cc";
            case HAMSTRINGS -> "#0066cc";
            case HANDS -> "#0033cc";
            case LATS -> "#3e9393";
            case LOWER_BACK -> "#3333ff";
            case NECK -> "#6699ff";
            case OBLIQUES -> "#009999";
            case QUADS -> "#33cccc";
            case SHINS -> "#00ccff";
            case TRICEPS -> "#0099ff";
            case UPPER_BACK -> "#0066ff";
        };
    }
}
