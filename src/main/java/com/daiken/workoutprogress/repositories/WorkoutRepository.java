package com.daiken.workoutprogress.repositories;

import com.daiken.workoutprogress.models.User;
import com.daiken.workoutprogress.models.Workout;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;


public interface WorkoutRepository extends MongoRepository<Workout, String> {

    List<Workout> findWorkoutByUserId(String user_id);

    long countWorkoutsByUserAndActive(User user, boolean active);

    Optional<Workout> findWorkoutByUserIdAndActive(String user_id, boolean active);
}
