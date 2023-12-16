package com.daiken.workoutprogress.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserTests {
    private User user;

    @BeforeEach
    public void setUp() {
        // Initialize a new User instance before each test.
        user = new User();
    }

    @Test
    public void testDefaultConstructor() {
        assertNull(user.getId());
        assertNull(user.getFid());
    }

    @Test
    public void testParameterizedConstructor() {
        String fid = "123456";
        User userWithFid = new User(fid);

        assertNull(userWithFid.getId());
        assertEquals(fid, userWithFid.getFid());
    }

    @Test
    public void testSetAndGetFid() {
        String fid = "789012";
        user.setFid(fid);

        assertEquals(fid, user.getFid());
    }

    @Test
    public void testEqualsAndHashCode() {
        User user1 = new User("fid123");
        User user2 = new User("fid123");
        User user3 = new User("fid456");

        // Test equality
        assertEquals(user1, user2);

        // Test inequality
        assertNotEquals(user1, user3);
    }
}
