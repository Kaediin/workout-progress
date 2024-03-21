package com.daiken.workoutprogress.models;

import com.daiken.workoutprogress.api.graphql.input.PreferenceInput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PreferenceTests {
    @BeforeEach
    public void setUp() {
        // Perform any setup here if needed.
    }

    @Test
    public void testDefaultConstructor() {
        Preference preference = new Preference();

        assertNull(preference.getId());
        assertNull(preference.getUser());
        assertNull(preference.getDistanceUnit());
        assertNull(preference.getWeightUnit());
        assertEquals(0, preference.getDefaultRepetitions());
        assertFalse(preference.isHideUnitSelector());
        assertFalse(preference.isAutoAdjustWorkoutMuscleGroups());
        assertEquals(0, preference.getTimerDuration());
        assertFalse(preference.isAutoStartTimer());
        assertFalse(preference.isPlayTimerCompletionSound());
    }

    @Test
    public void testParameterizedConstructor() {
        User user = new User();
        Preference preference = new Preference(user);

        assertEquals(user, preference.getUser());
        assertEquals(LogUnit.KG, preference.getWeightUnit());
        assertEquals(LogUnit.KM, preference.getDistanceUnit());
        assertEquals(10, preference.getDefaultRepetitions());
        assertEquals(120, preference.getTimerDuration());
        assertFalse(preference.isHideUnitSelector());
        assertTrue(preference.isAutoAdjustWorkoutMuscleGroups());
        assertFalse(preference.isAutoStartTimer());
        assertTrue(preference.isPlayTimerCompletionSound());
    }

    @Test
    public void testParameterizedConstructorWithInput() {
        User user = new User();
        PreferenceInput input = new PreferenceInput(
                LogUnit.MI,
                LogUnit.LBS,
                15,
                true,
                true,
                180,
                true,
                true
        );

        Preference preference = new Preference(user, input);

        assertEquals(user, preference.getUser());
        assertEquals(LogUnit.LBS, preference.getWeightUnit());
        assertEquals(LogUnit.MI, preference.getDistanceUnit());
        assertEquals(15, preference.getDefaultRepetitions());
        assertEquals(180, preference.getTimerDuration());
        assertTrue(preference.isHideUnitSelector());
        assertTrue(preference.isAutoAdjustWorkoutMuscleGroups());
        assertTrue(preference.isAutoStartTimer());
        assertTrue(preference.isPlayTimerCompletionSound());
    }

    @Test
    public void testUpdate() {
        User user = new User();
        Preference preference = new Preference(user);

        PreferenceInput input = new PreferenceInput(
                LogUnit.MI,
                LogUnit.LBS,
                15,
                true,
                true,
                180,
                true,
                true
        );

        preference.update(input);

        assertEquals(user, preference.getUser());
        assertEquals(LogUnit.LBS, preference.getWeightUnit());
        assertEquals(LogUnit.MI, preference.getDistanceUnit());
        assertEquals(15, preference.getDefaultRepetitions());
        assertEquals(180, preference.getTimerDuration());
        assertTrue(preference.isHideUnitSelector());
        assertTrue(preference.isAutoAdjustWorkoutMuscleGroups());
        assertTrue(preference.isAutoStartTimer());
        assertTrue(preference.isPlayTimerCompletionSound());
    }
}
