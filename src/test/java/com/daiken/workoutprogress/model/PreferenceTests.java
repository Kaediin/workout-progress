package com.daiken.workoutprogress.model;

import com.daiken.workoutprogress.api.graphql.input.PreferenceInput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PreferenceTests {
    @BeforeEach
    public void setUp() {
        // You can perform any setup here if needed.
    }

    @Test
    public void testDefaultConstructor() {
        Preference preference = new Preference();

        assertNull(preference.id);
        assertNull(preference.user);
        assertNull(preference.distanceUnit);
        assertNull(preference.weightUnit);
        assertEquals(0, preference.defaultRepetitions);
        assertFalse(preference.hideUnitSelector);
        assertFalse(preference.autoAdjustWorkoutMuscleGroups);
        assertEquals(0, preference.timerDuration);
        assertFalse(preference.autoStartTimer);
    }

    @Test
    public void testParameterizedConstructor() {
        User user = new User();
        Preference preference = new Preference(user);

        assertEquals(user, preference.user);
        assertEquals(LogUnit.KG, preference.weightUnit);
        assertEquals(LogUnit.KM, preference.distanceUnit);
        assertEquals(10, preference.defaultRepetitions);
        assertEquals(120, preference.timerDuration);
        assertFalse(preference.autoStartTimer);
    }

    @Test
    public void testParameterizedConstructorWithInput() {
        User user = new User();
        PreferenceInput input = new PreferenceInput();
        input.setWeightUnit(LogUnit.LBS);
        input.setDistanceUnit(LogUnit.MI);
        input.setDefaultRepetitions(15);
        input.setTimerDuration(180);
        input.setAutoStartTimer(true);

        Preference preference = new Preference(user, input);

        assertEquals(user, preference.user);
        assertEquals(LogUnit.LBS, preference.weightUnit);
        assertEquals(LogUnit.MI, preference.distanceUnit);
        assertEquals(15, preference.defaultRepetitions);
        assertEquals(180, preference.timerDuration);
        assertTrue(preference.autoStartTimer);
    }

    @Test
    public void testUpdate() {
        User user = new User();
        Preference preference = new Preference(user);

        PreferenceInput input = new PreferenceInput();
        input.setWeightUnit(LogUnit.LBS);
        input.setDistanceUnit(LogUnit.MI);
        input.setDefaultRepetitions(15);
        input.setHideUnitSelector(true);
        input.setAutoAdjustWorkoutMuscleGroups(true);
        input.setTimerDuration(180);
        input.setAutoStartTimer(true);

        preference.update(input);

        assertEquals(LogUnit.LBS, preference.weightUnit);
        assertEquals(LogUnit.MI, preference.distanceUnit);
        assertEquals(15, preference.defaultRepetitions);
        assertTrue(preference.hideUnitSelector);
        assertTrue(preference.autoAdjustWorkoutMuscleGroups);
        assertEquals(180, preference.timerDuration);
        assertTrue(preference.autoStartTimer);
    }
}
