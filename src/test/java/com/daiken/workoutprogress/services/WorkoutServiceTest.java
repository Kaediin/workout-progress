package com.daiken.workoutprogress.services;

import com.daiken.workoutprogress.models.*;
import com.daiken.workoutprogress.repositories.ExerciseLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WorkoutServiceTest {

    @Mock
    private ExerciseLogRepository exerciseLogRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    @Test
    void adjustWorkoutMuscleGroupsShouldAdjustMuscleGroupsWhenExercisesArePresent() {
        User user = new User();
        user.setId("user-123");

        String workoutId = "workout-123";
        Workout workout = new Workout();
        workout.setId(workoutId);

        Exercise exercise = new Exercise();
        exercise.setId("exercise-id");
        exercise.setPrimaryMuscles(Arrays.asList(MuscleGroup.ABS, MuscleGroup.BICEPS));

        ExerciseLog log = new ExerciseLog();
        log.setExercise(exercise);
    }

    @Test
    void adjustWorkoutMuscleGroupsShouldNotAdjustMuscleGroupsWhenNoExercisesArePresent() {
        User user = new User();
        user.setId("user-123");

        String workoutId = "workout-123";
        Workout workout = new Workout();
        workout.setId(workoutId);

        when(exerciseLogRepository.findAllByWorkoutIdAndUserId(workoutId, user.getId())).thenReturn(List.of());
    }
}
