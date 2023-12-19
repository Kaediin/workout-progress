package com.daiken.workoutprogress.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GroupedExerciseLogTests {
    @Mock
    private Exercise exercise;

    @Mock
    private List<ExerciseLog> logs;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDefaultConstructor() {
        GroupedExerciseLog groupedExerciseLog = new GroupedExerciseLog();
        assertNull(groupedExerciseLog.getExercise());
        assertNull(groupedExerciseLog.getLogs());
    }

    @Test
    public void testParameterizedConstructor() {
        List<ExerciseLog> mockLogs = new ArrayList<>();
        when(exercise.getName()).thenReturn("Test Exercise");
        GroupedExerciseLog groupedExerciseLog = new GroupedExerciseLog(exercise, mockLogs);
        assertEquals(exercise, groupedExerciseLog.getExercise());
        assertEquals(mockLogs, groupedExerciseLog.getLogs());
    }

    @Test
    public void testGetExercise() {
        GroupedExerciseLog groupedExerciseLog = new GroupedExerciseLog(exercise, logs);
        assertEquals(exercise, groupedExerciseLog.getExercise());
    }

    @Test
    public void testGetLogs() {
        GroupedExerciseLog groupedExerciseLog = new GroupedExerciseLog(exercise, logs);
        assertEquals(logs, groupedExerciseLog.getLogs());
    }

    @Test
    public void testSetExercise() {
        GroupedExerciseLog groupedExerciseLog = new GroupedExerciseLog();
        groupedExerciseLog.setExercise(exercise);
        assertEquals(exercise, groupedExerciseLog.getExercise());
    }

    @Test
    public void testSetLogs() {
        GroupedExerciseLog groupedExerciseLog = new GroupedExerciseLog();
        groupedExerciseLog.setLogs(logs);
        assertEquals(logs, groupedExerciseLog.getLogs());
    }
}
