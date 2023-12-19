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
        assertNull(cognitoUser.userName);
        assertNull(cognitoUser.name);
        assertNull(cognitoUser.fid);
        assertNull(cognitoUser.given_name);
        assertNull(cognitoUser.family_name);
        assertNull(cognitoUser.middle_name);
        assertNull(cognitoUser.nickname);
        assertNull(cognitoUser.email);
        assertNull(cognitoUser.gender);
        assertNull(cognitoUser.zoneinfo);
        assertNull(cognitoUser.locale);
        assertNull(cognitoUser.createdDate);
    }

    @Test
    public void testParameterizedConstructor() {
        CognitoUser cognitoUser = new CognitoUser(userType);
        assertEquals("testUser", cognitoUser.userName);
        assertEquals("John Doe", cognitoUser.name);
        assertNotNull(cognitoUser.fid);
        assertEquals("John", cognitoUser.given_name);
        assertEquals("Doe", cognitoUser.family_name);
        assertEquals("Middle", cognitoUser.middle_name);
        assertNull(cognitoUser.nickname);
        assertEquals("test@example.com", cognitoUser.email);
        assertEquals("Male", cognitoUser.gender);
        assertNull(cognitoUser.zoneinfo);
        assertEquals("en_US", cognitoUser.locale);
        assertNotNull(cognitoUser.createdDate);
    }

    @Test
    public void testGetFullNameWithFullNameSet() {
        CognitoUser cognitoUser = new CognitoUser();
        cognitoUser.name = "Full Name";
        assertEquals("Full Name", cognitoUser.getFullName());
    }

    @Test
    public void testGetFullNameWithIndividualNameParts() {
        CognitoUser cognitoUser = new CognitoUser();
        cognitoUser.given_name = "John";
        cognitoUser.middle_name = "Middle";
        cognitoUser.family_name = "Doe";
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

        assertNotNull(cognitoUser.fid);
        assertEquals("test@example.com", cognitoUser.email);
        assertEquals("John Doe", cognitoUser.name);
    }
}
