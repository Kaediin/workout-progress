package com.daiken.workoutprogress.repositories;

import com.daiken.workoutprogress.models.Exercise;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ExerciseRepository extends MongoRepository<Exercise, String> {
    List<Exercise> findAllByUserId(String user_id);

    Optional<Exercise> findExerciseByIdAndUserId(String id, String user_id);
}
