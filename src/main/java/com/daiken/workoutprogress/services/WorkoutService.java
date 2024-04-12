package com.daiken.workoutprogress.services;

import com.daiken.workoutprogress.models.*;
import com.daiken.workoutprogress.repositories.ExerciseLogRepository;
import com.daiken.workoutprogress.repositories.ExerciseRepository;
import com.daiken.workoutprogress.repositories.WorkoutRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for Workout operations.
 */
@Slf4j
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

    /**
     * Adjust the muscle groups for a workout.
     *
     * @param workoutId Workout ID
     */
    public void adjustWorkoutMuscleGroups(String workoutId, User me) {
        adjustWorkoutMuscleGroups(workoutRepository.findWorkoutByIdAndUserId(workoutId, me.getId()).orElseThrow(() -> {
            log.error("Cant find workout with given id {}", workoutId);
            return new NullPointerException("Cant find workout with given id");
        }), me);
    }

    /**
     * Adjust the muscle groups for a workout.
     * @param workout Workout
     */
    public void adjustWorkoutMuscleGroups(Workout workout, User me) {
        List<Exercise> exercisesDoneThisWorkout = exerciseLogRepository.findAllByWorkoutIdAndUserId(workout.getId(), me.getId())
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

    /**
     * Filter a list of objects by a key.
     * @param keyExtractor Key extractor
     * @return Predicate
     * @param <T> Type of object
     */
    public static <T> java.util.function.Predicate<T> distinctByKey(java.util.function.Function<? super T, ?> keyExtractor) {
        java.util.Map<Object, Boolean> seen = new java.util.concurrent.ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    /**
     * Map a list of muscle groups to a list of muscle group chart data.
     * @param muscleGroups List of muscle groups
     * @return List of muscle group chart data
     */
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

    /**
     * Map a muscle group to a color.
     * @param muscleGroup Muscle group
     * @return Color
     */
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
