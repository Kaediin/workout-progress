package com.daiken.workoutprogress.repository;

import com.daiken.workoutprogress.model.ExerciseLog;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;


public interface ExerciseLogRepository extends MongoRepository<ExerciseLog, String> {
    Stream<ExerciseLog> findAllByUserIdAndWorkoutId(String user_id, String workout_id);

    @Aggregation(pipeline = {
            "{ '$match': {$and: [" +
                    "{ 'user.id': ?0}," +
                    "{ 'workout.id': ?1 }," +
                    "]}" +
                    "}",
            "{ '$sort' : { 'logDateTime' : -1 } }",
            "{ '$limit' : 1 }"
    })
    Optional<ExerciseLog> findLastLogByUserIdAndWorkoutId(String user_id, String workout_id);

    @Aggregation(pipeline = {
            "{ '$match': {$and: [" +
                    "{ 'workout.id': ?0}," +
                    "{ 'exercise.id': ?1 }," +
                    "]}" +
                    "}",
            "{ '$sort' : { 'logDateTime' : -1 } }"
    })
    List<ExerciseLog> findLastLogsByWorkoutIdAndExerciseId(String workout_id, String exercise_id);

    @Aggregation(pipeline = {
            "{ '$match': {$and: [" +
                    "{ 'user.id': ?0}," +
                    "{ 'exercise.id': ?1 }," +
                    "]}" +
                    "}",
            "{ '$sort' : { 'logDateTime' : -1 } }",
            "{ '$limit' : 1 }"
    })
    Optional<ExerciseLog> findLastLogByUserIdAndExerciseId(String user_id, String exercise_id);

    List<ExerciseLog> findAllByUserIdAndExerciseId(String user_id, String exercise_id);

    List<ExerciseLog> findAllByWorkoutId(String workout_id);

    Stream<ExerciseLog> findAllByWeightLeftExistsOrWeightRightExists(boolean existsLeft, boolean existsRight);
}
