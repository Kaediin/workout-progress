package com.daiken.workoutprogress.repositories;

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
public class UserRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void setUp() {
        mongoTemplate.dropCollection(User.class);

        // Insert test data into the in-memory MongoDB
        User user1 = new User("fid1");
        User user2 = new User("fid2");
        mongoTemplate.insertAll(List.of(user1, user2));
    }

    @AfterEach
    void cleanUp() {
        mongoTemplate.dropCollection(User.class);
    }

    @Test
    void findOneByFidShouldReturnUser() {
        Optional<User> foundUser = userRepository.findOneByFid("fid1");

        assertTrue(foundUser.isPresent());
        assertEquals("fid1", foundUser.get().getFid());
    }

    @Test
    void findOneByFidShouldReturnEmptyForUnknownFid() {
        Optional<User> foundUser = userRepository.findOneByFid("unknown-fid");

        assertFalse(foundUser.isPresent());
    }

    @Test
    void countShouldReturnCorrectNumberOfUsers() {
        long userCount = userRepository.count();

        assertEquals(2, userCount);
    }

}
