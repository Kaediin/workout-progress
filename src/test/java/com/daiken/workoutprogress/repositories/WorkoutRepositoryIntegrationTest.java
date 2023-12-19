package com.daiken.workoutprogress.repositories;

import com.daiken.workoutprogress.models.MuscleGroup;
import com.daiken.workoutprogress.models.User;
import com.daiken.workoutprogress.models.Workout;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataMongoTest
@ActiveProfiles("test")
public class WorkoutRepositoryIntegrationTest {

    @Autowired
    private WorkoutRepository workoutRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    private User user1, user2;

    @BeforeEach
    void setUp() {
        mongoTemplate.dropCollection(Workout.class);
        mongoTemplate.dropCollection(User.class);

        // Create test users
        user1 = new User();
        user1.setId("user1-id");
        user2 = new User();
        user2.setId("user2-id");
        mongoTemplate.insertAll(List.of(user1, user2));

        // Insert test data into the in-memory MongoDB
        Workout workout1 = new Workout("workout1-id", "Morning Workout", Arrays.asList(MuscleGroup.ABS, MuscleGroup.CHEST));
        workout1.user = user1;
        workout1.active = true;
        workout1.startDateTime = LocalDateTime.now();

        Workout workout2 = new Workout("workout2-id", "Evening Workout", Arrays.asList(MuscleGroup.BACK_SHOULDERS, MuscleGroup.BICEPS));
        workout2.user = user1;
        workout2.active = false;
        workout2.startDateTime = LocalDateTime.now();

        Workout workout3 = new Workout("workout3-id", "Afternoon Workout", Arrays.asList(MuscleGroup.TRICEPS, MuscleGroup.QUADS));
        workout3.user = user2;
        workout3.active = true;
        workout3.startDateTime = LocalDateTime.now();

        mongoTemplate.insertAll(List.of(workout1, workout2, workout3));
    }

    @Test
    void findWorkoutByUserIdShouldReturnWorkoutsForUser() {
        List<Workout> workouts = workoutRepository.findWorkoutByUserId(user1.getId());

        assertEquals(2, workouts.size());
        assertTrue(workouts.stream().anyMatch(workout -> workout.id.equals("workout1-id")));
        assertTrue(workouts.stream().anyMatch(workout -> workout.id.equals("workout2-id")));
    }

    @Test
    void countWorkoutsByUserAndActiveShouldReturnCorrectCount() {
        long activeCount = workoutRepository.countWorkoutsByUserAndActive(user1, true);
        long inactiveCount = workoutRepository.countWorkoutsByUserAndActive(user1, false);

        assertEquals(1, activeCount);
        assertEquals(1, inactiveCount);
    }

    @Test
    void findWorkoutByUserIdAndActiveShouldReturnWorkout() {
        Optional<Workout> activeWorkout = workoutRepository.findWorkoutByUserIdAndActive(user1.getId(), true);
        Optional<Workout> inactiveWorkout = workoutRepository.findWorkoutByUserIdAndActive(user1.getId(), false);

        assertTrue(activeWorkout.isPresent());
        assertTrue(inactiveWorkout.isPresent());
        assertEquals("workout1-id", activeWorkout.get().id);
        assertEquals("workout2-id", inactiveWorkout.get().id);
    }
}
