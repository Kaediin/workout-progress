package com.daiken.workoutprogress.repositories;

import com.daiken.workoutprogress.models.ProgramWorkout;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProgramWorkoutRepository extends MongoRepository<ProgramWorkout, String> {
}
