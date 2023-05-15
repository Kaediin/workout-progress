package com.daiken.workoutprogress.repository;

import com.daiken.workoutprogress.model.Workout;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WorkoutRepository extends MongoRepository<Workout, String> {
}
