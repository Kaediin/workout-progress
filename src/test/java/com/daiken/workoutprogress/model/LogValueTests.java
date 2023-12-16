package com.daiken.workoutprogress.model;

import com.daiken.workoutprogress.api.graphql.input.LogValueInput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LogValueTests {

    @BeforeEach
    public void setUp() {
        // You can perform any setup here if needed.
    }

    @Test
    public void testParameterizedConstructor() {
        LogValueInput input = new LogValueInput();
        input.setBase(10);
        input.setFraction(5);
        input.setUnit(LogUnit.KG);

        LogValue logValue = new LogValue(input);

        assertEquals(10, logValue.getBase());
        assertEquals(5, logValue.getFraction());
        assertEquals(LogUnit.KG, logValue.getUnit());
    }

    @Test
    public void testDefaultConstructor() {
        LogValue logValue = new LogValue();

        assertNull(logValue.getBase());
        assertNull(logValue.getFraction());
        assertNull(logValue.getUnit());
    }

    @Test
    public void testGettersAndSetters() {
        LogValue logValue = new LogValue();

        logValue.setBase(20);
        logValue.setFraction(8);
        logValue.setUnit(LogUnit.LBS);

        assertEquals(20, logValue.getBase());
        assertEquals(8, logValue.getFraction());
        assertEquals(LogUnit.LBS, logValue.getUnit());
    }

    @Test
    public void testEqualsAndHashCode() {
        LogValue logValue1 = new LogValue(10, 5, LogUnit.KG);
        LogValue logValue2 = new LogValue(10, 5, LogUnit.KG);
        LogValue logValue3 = new LogValue(20, 5, LogUnit.KG);

        // Test equality
        assertEquals(logValue1, logValue2);

        // Test inequality
        assertNotEquals(logValue1, logValue3);
    }
}
