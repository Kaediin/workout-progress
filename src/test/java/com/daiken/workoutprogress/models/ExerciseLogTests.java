package com.daiken.workoutprogress.models;

import com.daiken.workoutprogress.api.graphql.input.ExerciseLogInput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ExerciseLogTests {

    @Mock
    private ExerciseLogInput exerciseLogInput;

    @Mock
    private User user;

    @Mock
    private Workout workout;

    @Mock
    private Exercise exercise;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDefaultConstructor() {
        ExerciseLog exerciseLog = new ExerciseLog();
        assertNull(exerciseLog.id);
        assertNull(exerciseLog.logDateTime);
        assertNull(exerciseLog.exercise);
        assertNull(exerciseLog.workout);
        assertNull(exerciseLog.user);
        assertEquals(0, exerciseLog.repetitions);
        assertNull(exerciseLog.logValue);
        assertNull(exerciseLog.warmup);
        assertNull(exerciseLog.remark);
    }

    @Test
    public void testCopyConstructor() {
        ExerciseLog copyLog = new ExerciseLog();
        copyLog.user = user;
        copyLog.exercise = exercise;
        copyLog.workout = workout;
        copyLog.logDateTime = LocalDateTime.now();
        copyLog.repetitions = 10;
        copyLog.logValue = new LogValue(5, 0, LogUnit.KG);
        copyLog.warmup = true;
        copyLog.remark = "Test Remark";

        ExerciseLog exerciseLog = new ExerciseLog(copyLog, "2023-01-01T12:00:00Z");
        assertEquals(copyLog.user, exerciseLog.user);
        assertEquals(copyLog.exercise, exerciseLog.exercise);
        assertEquals(copyLog.workout, exerciseLog.workout);
        assertEquals(LocalDateTime.parse("2023-01-01T12:00:00"), exerciseLog.logDateTime);
        assertEquals(copyLog.repetitions, exerciseLog.repetitions);
        assertEquals(copyLog.logValue, exerciseLog.logValue);
        assertEquals(copyLog.warmup, exerciseLog.warmup);
        assertEquals(copyLog.remark, exerciseLog.remark);
    }

    @Test
    public void testParameterizedConstructor() {
        when(exerciseLogInput.getZonedDateTimeString()).thenReturn("2023-01-01T12:00:00Z");
        ExerciseLog exerciseLog = new ExerciseLog(exerciseLogInput, user, workout, exercise);
        assertEquals(user, exerciseLog.user);
        assertEquals(exercise, exerciseLog.exercise);
        assertEquals(workout, exerciseLog.workout);
        assertEquals(LocalDateTime.parse("2023-01-01T12:00:00"), exerciseLog.logDateTime);
        // Verify other properties as well
    }

    @Test
    public void testUpdate() {
        ExerciseLog exerciseLog = new ExerciseLog();
        when(exerciseLogInput.getZonedDateTimeString()).thenReturn("2023-01-01T12:00:00Z");
        exerciseLog.update(exerciseLogInput);
        assertEquals(LocalDateTime.parse("2023-01-01T12:00:00"), exerciseLog.logDateTime);
        // Verify other properties as well
    }

    // Add more test cases to cover other methods and behaviors of the ExerciseLog class

}
