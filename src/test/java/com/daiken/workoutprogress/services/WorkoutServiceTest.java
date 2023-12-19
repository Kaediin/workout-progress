package com.daiken.workoutprogress.services;

import com.daiken.workoutprogress.models.Exercise;
import com.daiken.workoutprogress.models.ExerciseLog;
import com.daiken.workoutprogress.models.MuscleGroup;
import com.daiken.workoutprogress.models.Workout;
import com.daiken.workoutprogress.repositories.ExerciseLogRepository;
import com.daiken.workoutprogress.repositories.ExerciseRepository;
import com.daiken.workoutprogress.repositories.WorkoutRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WorkoutServiceTest {

    @Mock
    private ExerciseLogRepository exerciseLogRepository;

    @Mock
    private ExerciseRepository exerciseRepository;

    @Mock
    private WorkoutRepository workoutRepository;

    @InjectMocks
    private WorkoutService workoutService;

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
    void adjustWorkoutMuscleGroupsByIdShouldAdjustMuscleGroups() {
        String workoutId = "workout-123";
        Workout workout = new Workout();
        workout.id = workoutId;

        // Set up Exercise and ExerciseLog with valid IDs
        Exercise exercise1 = new Exercise();
        exercise1.setId("exercise1-id"); // Set a valid ID
        exercise1.setPrimaryMuscles(Arrays.asList(MuscleGroup.ABS, MuscleGroup.BICEPS));

        Exercise exercise2 = new Exercise();
        exercise2.setId("exercise2-id"); // Set a valid ID
        exercise2.setSecondaryMuscles(Arrays.asList(MuscleGroup.TRICEPS, MuscleGroup.CALVES, MuscleGroup.TRICEPS));

        ExerciseLog log1 = new ExerciseLog();
        log1.exercise = exercise1;

        ExerciseLog log2 = new ExerciseLog();
        log2.exercise = exercise2;

        when(workoutRepository.findById(workoutId)).thenReturn(Optional.of(workout));
        when(exerciseLogRepository.findAllByWorkoutId(workoutId)).thenReturn(List.of(log1, log2));
        when(exerciseRepository.findById("exercise1-id")).thenReturn(Optional.of(exercise1));
        when(exerciseRepository.findById("exercise2-id")).thenReturn(Optional.of(exercise2));

        workoutService.adjustWorkoutMuscleGroups(workoutId);

        verify(workoutRepository).findById(workoutId);
        verify(exerciseLogRepository).findAllByWorkoutId(workoutId);
        verify(exerciseRepository).findById("exercise1-id");
        verify(exerciseRepository).findById("exercise2-id");

        assertTrue(workout.muscleGroups.containsAll(exercise1.getPrimaryMuscles()));
        assertTrue(workout.muscleGroups.contains(MuscleGroup.TRICEPS));
    }
}
