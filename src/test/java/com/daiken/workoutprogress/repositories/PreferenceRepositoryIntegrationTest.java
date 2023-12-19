package com.daiken.workoutprogress.repositories;

import com.daiken.workoutprogress.models.Preference;
import com.daiken.workoutprogress.models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ActiveProfiles("test")
public class PreferenceRepositoryIntegrationTest {

    @Autowired
    private PreferenceRepository preferenceRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    private User user1, user2;

    @BeforeEach
    void setUp() {
        mongoTemplate.dropCollection(Preference.class);
        mongoTemplate.dropCollection(User.class);

        // Create test users
        user1 = new User("user1-id");
        user1.setId("user1-id");
        user2 = new User("user2-id");
        user2.setId("user2-id");
        mongoTemplate.insertAll(List.of(user1, user2));

        // Insert test data into the in-memory MongoDB
        Preference preference1 = new Preference(user1);
        Preference preference2 = new Preference(user2);
        mongoTemplate.insertAll(List.of(preference1, preference2));
    }

    @AfterEach
    void cleanUp() {
        mongoTemplate.dropCollection(Preference.class);
        mongoTemplate.dropCollection(User.class);
    }

    @Test
    void findByUserIdShouldReturnPreference() {
        Optional<Preference> result = preferenceRepository.findByUserId(user1.getId());

        assertTrue(result.isPresent());
        assertEquals(user1.getId(), result.get().user.id);
        assertNotEquals(user2.getId(), result.get().user.id);
    }

    @Test
    void findByUserIdShouldReturnEmptyForUnknownUser() {
        Optional<Preference> result = preferenceRepository.findByUserId("unknown-user-id");

        assertFalse(result.isPresent());
    }
}
