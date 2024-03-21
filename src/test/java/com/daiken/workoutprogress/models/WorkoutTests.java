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
        assertNull(workout.getId());
        assertNull(workout.getName());
        assertNull(workout.getMuscleGroups());
        assertNull(workout.getUser());
        assertNull(workout.getStartDateTime());
        assertNull(workout.getEndDateTime());
        assertFalse(workout.isActive());
        assertNull(workout.getRemark());
    }

    @Test
    public void testParameterizedConstructor() {
        String id = "workout123";
        String name = "Test Workout";
        List<MuscleGroup> muscleGroups = new ArrayList<>();
        muscleGroups.add(MuscleGroup.BICEPS);

        Workout workout = new Workout(id, name, muscleGroups);

        assertEquals(id, workout.getId());
        assertEquals(name, workout.getName());
        assertEquals(muscleGroups, workout.getMuscleGroups());
    }

    @Test
    public void testUpdate() {
        List<MuscleGroup> updatedMuscleGroups = new ArrayList<>();
        updatedMuscleGroups.add(MuscleGroup.ABDUCTOR);
        WorkoutInput input = new WorkoutInput(
                "Updated Workout",
                updatedMuscleGroups,
                "2023-01-01T12:00:00Z",
                "Updated remark"
        );

        Workout updatedWorkout = workout.update(input);

        assertEquals(input.name(), updatedWorkout.getName());
        assertEquals(updatedMuscleGroups, updatedWorkout.getMuscleGroups());
        assertEquals(ZonedDateTime.parse(input.zonedDateTime()).toLocalDateTime(), updatedWorkout.getStartDateTime());
        assertEquals(input.remark(), updatedWorkout.getRemark());
    }

    @Test
    public void testEndWorkout() {
        LocalDateTime endDateTime = LocalDateTime.now();
        Workout endedWorkout = workout.endWorkout(endDateTime);

        assertEquals(endDateTime, endedWorkout.getEndDateTime());
        assertFalse(endedWorkout.isActive());
    }

    @Test
    public void testCompareTo() {
        Workout workout1 = new Workout();
        workout1.setStartDateTime(LocalDateTime.parse("2023-01-01T10:00:00"));

        Workout workout2 = new Workout();
        workout2.setStartDateTime(LocalDateTime.parse("2023-01-01T12:00:00"));

        // Ensure that workout2 comes before workout1 due to earlier startDateTime.
        assertTrue(workout2.compareTo(workout1) < 0);
    }
}
