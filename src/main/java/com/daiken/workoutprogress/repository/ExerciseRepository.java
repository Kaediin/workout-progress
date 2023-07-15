package com.daiken.workoutprogress.repository;

import com.daiken.workoutprogress.model.Exercise;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ExerciseRepository extends MongoRepository<Exercise, String> {
    List<Exercise> findAllByUserId(String user_id);
}
