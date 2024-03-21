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
        assertNull(exerciseLog.getId());
        assertNull(exerciseLog.getLogDateTime());
        assertNull(exerciseLog.getExercise());
        assertNull(exerciseLog.getWorkout());
        assertNull(exerciseLog.getUser());
        assertEquals(0, exerciseLog.getRepetitions());
        assertNull(exerciseLog.getLogValue());
        assertNull(exerciseLog.getWarmup());
        assertNull(exerciseLog.getRemark());
    }

    @Test
    public void testCopyConstructor() {
        ExerciseLog copyLog = new ExerciseLog();
        copyLog.setUser(user);
        copyLog.setExercise(exercise);
        copyLog.setWorkout(workout);
        copyLog.setLogDateTime(LocalDateTime.now());
        copyLog.setRepetitions(10);
        copyLog.setLogValue(new LogValue(5, 0, LogUnit.KG));
        copyLog.setWarmup(true);
        copyLog.setRemark("Test Remark");

        ExerciseLog exerciseLog = new ExerciseLog(copyLog, "2023-01-01T12:00:00Z");
        assertEquals(copyLog.getUser(), exerciseLog.getUser());
        assertEquals(copyLog.getExercise(), exerciseLog.getExercise());
        assertEquals(copyLog.getWorkout(), exerciseLog.getWorkout());
        assertEquals(LocalDateTime.parse("2023-01-01T12:00:00"), exerciseLog.getLogDateTime());
        assertEquals(copyLog.getRepetitions(), exerciseLog.getRepetitions());
        assertEquals(copyLog.getLogValue(), exerciseLog.getLogValue());
        assertEquals(copyLog.getWarmup(), exerciseLog.getWarmup());
        assertEquals(copyLog.getRemark(), exerciseLog.getRemark());
    }

    @Test
    public void testParameterizedConstructor() {
        when(exerciseLogInput.zonedDateTimeString()).thenReturn("2023-01-01T12:00:00Z");
        ExerciseLog exerciseLog = new ExerciseLog(exerciseLogInput, user, workout, exercise);
        assertEquals(user, exerciseLog.getUser());
        assertEquals(exercise, exerciseLog.getExercise());
        assertEquals(workout, exerciseLog.getWorkout());
        assertEquals(LocalDateTime.parse("2023-01-01T12:00:00"), exerciseLog.getLogDateTime());
        // Verify other properties as well
    }

    @Test
    public void testUpdate() {
        ExerciseLog exerciseLog = new ExerciseLog();
        when(exerciseLogInput.zonedDateTimeString()).thenReturn("2023-01-01T12:00:00Z");
        exerciseLog.update(exerciseLogInput);
        assertEquals(LocalDateTime.parse("2023-01-01T12:00:00"), exerciseLog.getLogDateTime());
        // Verify other properties as well
    }

    // Add more test cases to cover other methods and behaviors of the ExerciseLog class

}
