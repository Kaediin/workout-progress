package com.daiken.workoutprogress.repository;

import com.daiken.workoutprogress.model.ExerciseLog;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.stream.Stream;


public interface ExerciseLogRepository extends MongoRepository<ExerciseLog, String> {
    Stream<ExerciseLog> findAllByUserIdAndWorkoutId(String user_id, String workout_id);
}
