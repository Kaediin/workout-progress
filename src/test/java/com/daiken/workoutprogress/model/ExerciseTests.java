package com.daiken.workoutprogress.model;

import com.daiken.workoutprogress.api.graphql.input.ExerciseInput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ExerciseTests {

    @Mock
    private ExerciseInput exerciseInput;

    @Mock
    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDefaultConstructor() {
        Exercise exercise = new Exercise();
        assertNull(exercise.getId());
        assertNull(exercise.getName());
        assertNull(exercise.getUser());
        assertNull(exercise.getPrimaryMuscles());
        assertNull(exercise.getSecondaryMuscles());
        assertNull(exercise.getDefaultAppliedWeight());
        assertNull(exercise.getNotes());
    }

    @Test
    public void testParameterizedConstructor() {
        when(exerciseInput.getName()).thenReturn("Test Exercise");
        Exercise exercise = new Exercise(exerciseInput, user);

        assertNull(exercise.getId()); // No id set in the constructor
        assertEquals("Test Exercise", exercise.getName());
        assertEquals(user, exercise.getUser());
        assertIterableEquals(exercise.getPrimaryMuscles(), new ArrayList<>());
        assertIterableEquals(exercise.getSecondaryMuscles(), new ArrayList<>());
        assertNull(exercise.getDefaultAppliedWeight());
        assertNull(exercise.getNotes());
    }

    @Test
    public void testUpdate() {
        Exercise exercise = new Exercise();

        when(exerciseInput.getName()).thenReturn("Updated Exercise");
        when(exerciseInput.getPrimaryMuscles()).thenReturn(new ArrayList<>());
        when(exerciseInput.getSecondaryMuscles()).thenReturn(new ArrayList<>());
        when(exerciseInput.getNotes()).thenReturn("Updated Notes");

        exercise.update(exerciseInput);

        assertNull(exercise.getId()); // Id should remain null
        assertEquals("Updated Exercise", exercise.getName());
        assertTrue(exercise.getPrimaryMuscles().isEmpty());
        assertTrue(exercise.getSecondaryMuscles().isEmpty());
        assertNull(exercise.getDefaultAppliedWeight()); // Not provided in the input
        assertEquals("Updated Notes", exercise.getNotes());
    }

    @Test
    public void testGetId() {
        Exercise exercise = new Exercise();
        exercise.setId("testId");
        assertEquals("testId", exercise.getId());
    }

    @Test
    public void testHashCode() {
        Exercise exercise1 = new Exercise();
        exercise1.setId("id1");

        Exercise exercise2 = new Exercise();
        exercise2.setId("id1");

        Exercise exercise3 = new Exercise();
        exercise3.setId("id2");

        assertEquals(exercise1.hashCode(), exercise2.hashCode());
        assertNotEquals(exercise1.hashCode(), exercise3.hashCode());
    }

    @Test
    public void testEquals() {
        Exercise exercise1 = new Exercise();
        exercise1.setId("id1");

        Exercise exercise2 = new Exercise();
        exercise2.setId("id1");

        Exercise exercise3 = new Exercise();
        exercise3.setId("id2");

        assertEquals(exercise1, exercise2);
        assertNotEquals(exercise1, exercise3);
        assertNotEquals(exercise2, exercise3);
    }
}
