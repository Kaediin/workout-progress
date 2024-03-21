package com.daiken.workoutprogress.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.UserType;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CognitoUserTests {

    private UserType userType;

    @BeforeEach
    public void setUp() {
        userType = UserType.builder()
                .userCreateDate(new Date().toInstant())
                .username("testUser")
                .attributes(
                        AttributeType.builder().name("sub").value(UUID.randomUUID().toString()).build(),
                        AttributeType.builder().name("email").value("test@example.com").build(),
                        AttributeType.builder().name("name").value("John Doe").build(),
                        AttributeType.builder().name("given_name").value("John").build(),
                        AttributeType.builder().name("family_name").value("Doe").build(),
                        AttributeType.builder().name("middle_name").value("Middle").build(),
                        AttributeType.builder().name("gender").value("Male").build(),
                        AttributeType.builder().name("locale").value("en_US").build()
                )
                .build();
    }

    @Test
    public void testDefaultConstructor() {
        CognitoUser cognitoUser = new CognitoUser();
        assertNull(cognitoUser.getUserName());
        assertNull(cognitoUser.getName());
        assertNull(cognitoUser.getFid());
        assertNull(cognitoUser.getGiven_name());
        assertNull(cognitoUser.getFamily_name());
        assertNull(cognitoUser.getMiddle_name());
        assertNull(cognitoUser.getNickname());
        assertNull(cognitoUser.getEmail());
        assertNull(cognitoUser.getGender());
        assertNull(cognitoUser.getZoneinfo());
        assertNull(cognitoUser.getLocale());
        assertNull(cognitoUser.getCreatedDate());
    }

    @Test
    public void testParameterizedConstructor() {
        CognitoUser cognitoUser = new CognitoUser(userType);
        assertEquals("testUser", cognitoUser.getUserName());
        assertEquals("John Doe", cognitoUser.getName());
        assertNotNull(cognitoUser.getFid());
        assertEquals("John", cognitoUser.getGiven_name());
        assertEquals("Doe", cognitoUser.getFamily_name());
        assertEquals("Middle", cognitoUser.getMiddle_name());
        assertNull(cognitoUser.getNickname());
        assertEquals("test@example.com", cognitoUser.getEmail());
        assertEquals("Male", cognitoUser.getGender());
        assertNull(cognitoUser.getZoneinfo());
        assertEquals("en_US", cognitoUser.getLocale());
        assertNotNull(cognitoUser.getCreatedDate());
    }

    @Test
    public void testGetFullNameWithFullNameSet() {
        CognitoUser cognitoUser = new CognitoUser();
        cognitoUser.setName("Full Name");
        assertEquals("Full Name", cognitoUser.getFullName());
    }

    @Test
    public void testGetFullNameWithIndividualNameParts() {
        CognitoUser cognitoUser = new CognitoUser();
        cognitoUser.setGiven_name("John");
        cognitoUser.setMiddle_name("Middle");
        cognitoUser.setFamily_name("Doe");
        assertEquals("John Middle Doe", cognitoUser.getFullName());
    }

    @Test
    public void testGetFullNameWithMissingNameParts() {
        CognitoUser cognitoUser = new CognitoUser();
        assertEquals(cognitoUser.getFullName(), "");
    }

    @Test
    public void testParseUserTypeAttributes() {
        CognitoUser cognitoUser = new CognitoUser();
        List<AttributeType> attributes = Arrays.asList(
                AttributeType.builder().name("sub").value(UUID.randomUUID().toString()).build(),
                AttributeType.builder().name("email").value("test@example.com").build(),
                AttributeType.builder().name("name").value("John Doe").build()
        );
        cognitoUser.parseUserTypeAttributes(attributes);

        assertNotNull(cognitoUser.getFid());
        assertEquals("test@example.com", cognitoUser.getEmail());
        assertEquals("John Doe", cognitoUser.getName());
    }
}
