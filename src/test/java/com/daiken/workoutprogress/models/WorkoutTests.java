package com.daiken.workoutprogress.models;

import com.daiken.workoutprogress.api.graphql.input.WorkoutInput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WorkoutTests {
    private Workout workout;

    @BeforeEach
    public void setUp() {
        // Initialize a new Workout instance before each test.
        workout = new Workout();
    }

    @Test
    public void testDefaultConstructor() {
        assertNull(workout.id);
        assertNull(workout.name);
        assertNull(workout.muscleGroups);
        assertNull(workout.user);
        assertNull(workout.startDateTime);
        assertNull(workout.endDateTime);
        assertFalse(workout.active);
        assertNull(workout.remark);
    }

    @Test
    public void testParameterizedConstructor() {
        String id = "workout123";
        String name = "Test Workout";
        List<MuscleGroup> muscleGroups = new ArrayList<>();
        muscleGroups.add(MuscleGroup.BICEPS);

        Workout workout = new Workout(id, name, muscleGroups);

        assertEquals(id, workout.id);
        assertEquals(name, workout.name);
        assertEquals(muscleGroups, workout.muscleGroups);
    }

    @Test
    public void testUpdate() {
        WorkoutInput input = new WorkoutInput();
        input.setName("Updated Workout");
        List<MuscleGroup> updatedMuscleGroups = new ArrayList<>();
        updatedMuscleGroups.add(MuscleGroup.ABDUCTOR);
        input.setMuscleGroups(updatedMuscleGroups);
        input.setZonedDateTime("2023-01-01T12:00:00Z");
        input.setRemark("Updated remark");

        Workout updatedWorkout = workout.update(input);

        assertEquals(input.getName(), updatedWorkout.name);
        assertEquals(updatedMuscleGroups, updatedWorkout.muscleGroups);
        assertEquals(ZonedDateTime.parse(input.getZonedDateTime()).toLocalDateTime(), updatedWorkout.startDateTime);
        assertEquals(input.getRemark(), updatedWorkout.remark);
    }

    @Test
    public void testEndWorkout() {
        LocalDateTime endDateTime = LocalDateTime.now();
        Workout endedWorkout = workout.endWorkout(endDateTime);

        assertEquals(endDateTime, endedWorkout.endDateTime);
        assertFalse(endedWorkout.active);
    }

    @Test
    public void testCompareTo() {
        Workout workout1 = new Workout();
        workout1.startDateTime = LocalDateTime.parse("2023-01-01T10:00:00");

        Workout workout2 = new Workout();
        workout2.startDateTime = LocalDateTime.parse("2023-01-01T12:00:00");

        // Ensure that workout2 comes before workout1 due to earlier startDateTime.
        assertTrue(workout2.compareTo(workout1) < 0);
    }
}
