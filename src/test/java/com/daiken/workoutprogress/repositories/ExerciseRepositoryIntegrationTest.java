//package com.daiken.workoutprogress.repositories;
//
//import com.daiken.workoutprogress.models.Exercise;
//import com.daiken.workoutprogress.models.User;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@DataMongoTest
//@ActiveProfiles("test")
//public class ExerciseRepositoryIntegrationTest {
//
//    @Autowired
//    private ExerciseRepository exerciseRepository;
//
//    @Autowired
//    private MongoTemplate mongoTemplate;
//
//    private Exercise exercise1, exercise2, exercise3;
//
//    @BeforeEach
//    void setUp() {
//        mongoTemplate.dropCollection(Exercise.class);
//        // Insert test data into the in-memory MongoDB
//        User user1 = new User();
//        user1.setId("userId1");
//
//        User user2 = new User();
//        user2.setId("userId2");
//
//        exercise1 = new Exercise();
//        exercise1.setUser(user1);
//        exercise1.setName("Push-ups");
//
//        exercise2 = new Exercise();
//        exercise2.setUser(user1);
//        exercise2.setName("Pull-ups");
//
//        exercise3 = new Exercise();
//        exercise3.setUser(user2);
//        exercise3.setName("Squats");
//        mongoTemplate.insertAll(List.of(exercise1, exercise2, exercise3));
//    }
//
//    @AfterEach
//    void cleanUp() {
//        mongoTemplate.remove(exercise1);
//        mongoTemplate.remove(exercise2);
//        mongoTemplate.remove(exercise3);
//    }
//
//    @Test
//    void findAllByUserIdShouldReturnExercisesForUser() {
//        List<Exercise> exercises = exerciseRepository.findAllByUserId("userId1");
//
//        assertNotNull(exercises);
//        assertEquals(2, exercises.size());
//        assertTrue(exercises.stream().anyMatch(exercise -> exercise.getName().equals("Push-ups")));
//        assertTrue(exercises.stream().anyMatch(exercise -> exercise.getName().equals("Pull-ups")));
//    }
//
//    @Test
//    void findAllByUserIdShouldReturnEmptyListForUnknownUser() {
//        List<Exercise> exercises = exerciseRepository.findAllByUserId("unknownUserId");
//
//        assertNotNull(exercises);
//        assertTrue(exercises.isEmpty());
//    }
//}
