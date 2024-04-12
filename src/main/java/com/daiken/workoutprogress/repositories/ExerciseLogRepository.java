package com.daiken.workoutprogress.repositories;

import com.daiken.workoutprogress.models.ExerciseLog;
import com.daiken.workoutprogress.models.Workout;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
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
    List<ExerciseLog> findLastLogsByUserIdWorkoutIdAndExerciseId(String user_id, String workout_id, String exercise_id);

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

    Optional<ExerciseLog> findFirstByUserIdAndExerciseIdAndWorkoutNotOrderByLogDateTimeDesc(String user_id, String exercise_id, Workout workout_id);

    Optional<ExerciseLog> findByIdAndUserId(String id, String user_id);

    List<ExerciseLog> findAllByUserIdAndExerciseIdOrderByLogDateTimeDesc(String user_id, String exercise_id);

    List<ExerciseLog> findAllByWorkoutIdAndUserId(String workout_id, String user_id);

    List<ExerciseLog> findAllByUserIdAndExerciseIdAndLogDateTimeBetween(String user_id, String exercise_id, LocalDateTime from, LocalDateTime to);
}
