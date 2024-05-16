package com.daiken.workoutprogress.repositories;

import com.daiken.workoutprogress.models.User;
import com.daiken.workoutprogress.models.Workout;
import com.daiken.workoutprogress.models.WorkoutStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface WorkoutRepository extends MongoRepository<Workout, String> {

    int countWorkoutsByUserId(String user_id);

    long countWorkoutsByUserAndStatus(User user, WorkoutStatus status);

    Optional<Workout> findWorkoutByIdAndUserId(String id, String user_id);

    Optional<Workout> findWorkoutByUserIdAndActive(String user_id, boolean active);

    List<Workout> findWorkoutByUserIdAndStartDateTimeIsNotNull(String user_id);

    @Query("{ 'user.id': ?0, 'startDateTime' : { $gte: ?1, $lt: ?2 } }")
    List<Workout> findByUserIdEndDateTimeYearAndMonth(String userId, LocalDate startOfMonth, LocalDate startOfNextMonth);


}
