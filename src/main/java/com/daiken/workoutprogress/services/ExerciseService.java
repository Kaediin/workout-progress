package com.daiken.workoutprogress.services;

import com.daiken.workoutprogress.api.graphql.input.ExerciseInput;
import com.daiken.workoutprogress.models.Exercise;
import com.daiken.workoutprogress.models.MuscleGroup;
import com.daiken.workoutprogress.models.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExerciseService {

    private final UserService userService;

    public ExerciseService(UserService userService) {
        this.userService = userService;
    }

    public List<Exercise> getOnboardingExercises() {
        User me = userService.getContextUser();
        return List.of(
                // Shoulders
                new Exercise("onboardingExercise1", new ExerciseInput("Dumbbell press", List.of(MuscleGroup.FRONT_SHOULDERS, MuscleGroup.BACK_SHOULDERS), List.of(), null, null), me),
                new Exercise("onboardingExercise2", new ExerciseInput("Barbell press", List.of(MuscleGroup.FRONT_SHOULDERS, MuscleGroup.BACK_SHOULDERS), List.of(), null, null), me),
                new Exercise("onboardingExercise3", new ExerciseInput("Lateral raise", List.of(MuscleGroup.FRONT_SHOULDERS, MuscleGroup.BACK_SHOULDERS), List.of(), null, null), me),
                new Exercise("onboardingExercise4", new ExerciseInput("Rear deltoid flies", List.of(MuscleGroup.FRONT_SHOULDERS, MuscleGroup.BACK_SHOULDERS), List.of(), null, null), me),
                new Exercise("onboardingExercise5", new ExerciseInput("Front raise", List.of(MuscleGroup.FRONT_SHOULDERS), List.of(MuscleGroup.BACK_SHOULDERS), null, null), me),
                // Biceps
                new Exercise("onboardingExercise6", new ExerciseInput("Bicep curl", List.of(MuscleGroup.BICEPS), List.of(), null, null), me),
                new Exercise("onboardingExercise7", new ExerciseInput("Hammer curl", List.of(MuscleGroup.BICEPS), List.of(), null, null), me),
                new Exercise("onboardingExercise8", new ExerciseInput("Bicep curl - pulley", List.of(MuscleGroup.BICEPS), List.of(), null, null), me),
                new Exercise("onboardingExercise9", new ExerciseInput("Preacher curl", List.of(MuscleGroup.BICEPS), List.of(), null, null), me),
                new Exercise("onboardingExercise10", new ExerciseInput("Barbell curl", List.of(MuscleGroup.BICEPS), List.of(), null, null), me),
                // Triceps
                new Exercise("onboardingExercise11", new ExerciseInput("Tricep pull-down", List.of(MuscleGroup.TRICEPS), List.of(), null, null), me),
                new Exercise("onboardingExercise12", new ExerciseInput("Dip", List.of(MuscleGroup.TRICEPS), List.of(), null, null), me),
                new Exercise("onboardingExercise13", new ExerciseInput("Skull crusher", List.of(MuscleGroup.TRICEPS), List.of(), null, null), me),
                new Exercise("onboardingExercise14", new ExerciseInput("Tricep kickback", List.of(MuscleGroup.TRICEPS), List.of(), null, null), me),
                new Exercise("onboardingExercise15", new ExerciseInput("Tricep extension - pulley", List.of(MuscleGroup.TRICEPS), List.of(), null, null), me),
                // Upper back
                new Exercise("onboardingExercise16", new ExerciseInput("Bent-over row", List.of(MuscleGroup.UPPER_BACK), List.of(MuscleGroup.BICEPS, MuscleGroup.BACK_SHOULDERS, MuscleGroup.LATS), null, null), me),
                new Exercise("onboardingExercise17", new ExerciseInput("Horizontal row", List.of(MuscleGroup.UPPER_BACK), List.of(MuscleGroup.BICEPS, MuscleGroup.BACK_SHOULDERS, MuscleGroup.LATS), null, null), me),
                new Exercise("onboardingExercise18", new ExerciseInput("Seated row", List.of(MuscleGroup.UPPER_BACK), List.of(), null, null), me),
                new Exercise("onboardingExercise19", new ExerciseInput("Shrugs", List.of(MuscleGroup.UPPER_BACK), List.of(MuscleGroup.BICEPS, MuscleGroup.BACK_SHOULDERS, MuscleGroup.LATS), null, null), me),
                new Exercise("onboardingExercise20", new ExerciseInput("Reverse pectoral fly", List.of(MuscleGroup.UPPER_BACK), List.of(), null, null), me),
                // Lats
                new Exercise("onboardingExercise21", new ExerciseInput("Lat pull-down", List.of(MuscleGroup.LATS), List.of(), null, null), me),
                new Exercise("onboardingExercise22", new ExerciseInput("Lat pull-over", List.of(MuscleGroup.LATS), List.of(), null, null), me),
                // Lower back
                new Exercise("onboardingExercise23", new ExerciseInput("Superman, alternated", List.of(MuscleGroup.LOWER_BACK), List.of(), null, null), me),
                new Exercise("onboardingExercise24", new ExerciseInput("Deadlift", List.of(MuscleGroup.LOWER_BACK, MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS), List.of(), null, null), me),
                new Exercise("onboardingExercise25", new ExerciseInput("Back extension", List.of(MuscleGroup.LOWER_BACK), List.of(MuscleGroup.GLUTES), null, null), me),
                // Chest
                new Exercise("onboardingExercise26", new ExerciseInput("Bench press", List.of(MuscleGroup.CHEST), List.of(MuscleGroup.TRICEPS, MuscleGroup.FRONT_SHOULDERS), null, null), me),
                new Exercise("onboardingExercise27", new ExerciseInput("Chest press", List.of(MuscleGroup.CHEST), List.of(MuscleGroup.TRICEPS, MuscleGroup.FRONT_SHOULDERS), null, null), me),
                new Exercise("onboardingExercise28", new ExerciseInput("Chest press incline", List.of(MuscleGroup.CHEST), List.of(MuscleGroup.TRICEPS, MuscleGroup.FRONT_SHOULDERS), null, null), me),
                new Exercise("onboardingExercise29", new ExerciseInput("Bench press incline", List.of(MuscleGroup.CHEST), List.of(MuscleGroup.TRICEPS, MuscleGroup.FRONT_SHOULDERS), null, null), me),
                new Exercise("onboardingExercise30", new ExerciseInput("Butterfly", List.of(MuscleGroup.CHEST), List.of(), null, null), me),
                new Exercise("onboardingExercise31", new ExerciseInput("Cable fly", List.of(MuscleGroup.CHEST), List.of(MuscleGroup.BICEPS), null, null), me),
                // Abs
                new Exercise("onboardingExercise32", new ExerciseInput("Crunch", List.of(MuscleGroup.ABS), List.of(), null, null), me),
                new Exercise("onboardingExercise33", new ExerciseInput("Leg raise", List.of(MuscleGroup.ABS), List.of(), null, null), me),
                new Exercise("onboardingExercise34", new ExerciseInput("Plank", List.of(MuscleGroup.ABS), List.of(), null, null), me),
                new Exercise("onboardingExercise35", new ExerciseInput("Russian twist", List.of(MuscleGroup.ABS), List.of(), null, null), me),
                new Exercise("onboardingExercise36", new ExerciseInput("Sit-up", List.of(MuscleGroup.ABS), List.of(), null, null), me),
                new Exercise("onboardingExercise37", new ExerciseInput("Mountain climber", List.of(MuscleGroup.ABS, MuscleGroup.QUADS), List.of(), null, null), me),
                // Quads
                new Exercise("onboardingExercise38", new ExerciseInput("Leg press", List.of(MuscleGroup.QUADS, MuscleGroup.GLUTES), List.of(MuscleGroup.HAMSTRINGS, MuscleGroup.CALVES, MuscleGroup.SHINS), null, null), me),
                new Exercise("onboardingExercise39", new ExerciseInput("Squat", List.of(MuscleGroup.QUADS, MuscleGroup.GLUTES), List.of(MuscleGroup.HAMSTRINGS, MuscleGroup.CALVES, MuscleGroup.SHINS), null, null), me),
                new Exercise("onboardingExercise40", new ExerciseInput("Lunge", List.of(MuscleGroup.QUADS, MuscleGroup.GLUTES), List.of(MuscleGroup.HAMSTRINGS, MuscleGroup.CALVES, MuscleGroup.SHINS), null, null), me),
                new Exercise("onboardingExercise41", new ExerciseInput("Leg extension", List.of(MuscleGroup.QUADS), List.of(), null, null), me),
                // Abductor
                new Exercise("onboardingExercise42", new ExerciseInput("Abductor", List.of(MuscleGroup.ABDUCTOR), List.of(), null, null), me),
                // Adductor
                new Exercise("onboardingExercise43", new ExerciseInput("Adductor", List.of(MuscleGroup.ADDUCTOR), List.of(), null, null), me),
                // Hamstring
                new Exercise("onboardingExercise44", new ExerciseInput("Leg curl seated", List.of(MuscleGroup.HAMSTRINGS), List.of(), null, null), me),
                new Exercise("onboardingExercise45", new ExerciseInput("Leg curl lying", List.of(MuscleGroup.HAMSTRINGS), List.of(), null, null), me),
                // Calves
                new Exercise("onboardingExercise46", new ExerciseInput("Seated calf raise", List.of(MuscleGroup.CALVES), List.of(), null, null), me),
                new Exercise("onboardingExercise47", new ExerciseInput("Standing calf raise", List.of(MuscleGroup.CALVES), List.of(), null, null), me)
        );
    }
}
