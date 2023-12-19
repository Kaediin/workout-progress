package com.daiken.workoutprogress.repositories;

import com.daiken.workoutprogress.models.Exercise;
import com.daiken.workoutprogress.models.ExerciseLog;
import com.daiken.workoutprogress.models.User;
import com.daiken.workoutprogress.models.Workout;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ActiveProfiles("test")
public class ExerciseLogRepositoryIntegrationTest {

    @Autowired
    private ExerciseLogRepository exerciseLogRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void setUp() {
        mongoTemplate.dropCollection(ExerciseLog.class);
        // Insert test data into the in-memory MongoDB
        User user1 = new User();
        user1.setFid("user1");
        user1.setId("user1");

        User user2 = new User();
        user2.setFid("user2");
        user2.setId("user2");

        Exercise exercise1 = new Exercise();
        exercise1.setId("exercise1");

        Exercise exercise2 = new Exercise();
        exercise2.setId("exercise2");

        Workout workout1 = new Workout();
        workout1.id = "workout1";

        Workout workout2 = new Workout();
        workout2.id = "workout2";

        // First log aka oldest
        ExerciseLog log1 = new ExerciseLog(null, user1, workout1, exercise1);
        log1.logDateTime = LocalDateTime.now().minusDays(3);

        // Second log aka middle
        ExerciseLog log2 = new ExerciseLog(null, user1, workout1, exercise2);
        log2.logDateTime = LocalDateTime.now().minusDays(2);

        // Third log aka youngest
        ExerciseLog log3 = new ExerciseLog(null, user2, workout2, exercise1);
        log3.logDateTime = LocalDateTime.now().minusDays(1);
        mongoTemplate.insertAll(List.of(log1, log2, log3));
    }

    @AfterEach
    void cleanUp() {
        mongoTemplate.dropCollection(ExerciseLog.class);
    }

    @Test
    void findAllByUserIdAndWorkoutIdShouldReturnLogs() {
        Stream<ExerciseLog> resultStream = exerciseLogRepository.findAllByUserIdAndWorkoutId("user1", "workout1");
        List<ExerciseLog> resultList = resultStream.toList();

        assertEquals(2, resultList.size());
        assertTrue(resultList.stream().anyMatch(log -> log.exercise.id.equals("exercise1")));
        assertTrue(resultList.stream().anyMatch(log -> log.exercise.id.equals("exercise2")));
    }

    @Test
    void findLastLogByUserIdAndWorkoutIdShouldReturnLastLog() {
        Optional<ExerciseLog> result = exerciseLogRepository.findLastLogByUserIdAndWorkoutId("user1", "workout1");

        assertTrue(result.isPresent());
        assertEquals("exercise2", result.get().exercise.id);
    }

    @Test
    void findLastLogsByWorkoutIdAndExerciseIdShouldReturnLogs() {
        List<ExerciseLog> resultList = exerciseLogRepository.findLastLogsByWorkoutIdAndExerciseId("workout1", "exercise1");

        assertFalse(resultList.isEmpty());
        assertTrue(resultList.stream().anyMatch(log -> log.user.id.equals("user1")));
    }

    @Test
    void findLastLogByUserIdAndExerciseIdShouldReturnLastLog() {
        Optional<ExerciseLog> result = exerciseLogRepository.findLastLogByUserIdAndExerciseId("user1", "exercise1");

        assertTrue(result.isPresent());
        assertEquals("workout1", result.get().workout.id);
    }

    @Test
    void findAllByUserIdAndExerciseIdShouldReturnLogs() {
        List<ExerciseLog> resultList = exerciseLogRepository.findAllByUserIdAndExerciseId("user1", "exercise1");

        assertFalse(resultList.isEmpty());
        assertTrue(resultList.stream().anyMatch(log -> log.workout.id.equals("workout1")));
    }

    @Test
    void findAllByWorkoutIdShouldReturnLogs() {
        List<ExerciseLog> resultList = exerciseLogRepository.findAllByWorkoutId("workout1");

        assertEquals(2, resultList.size());
        assertTrue(resultList.stream().anyMatch(log -> log.exercise.id.equals("exercise1")));
        assertTrue(resultList.stream().anyMatch(log -> log.exercise.id.equals("exercise2")));
    }
}
