package com.daiken.workoutprogress.repositories;

import com.daiken.workoutprogress.models.ProgramWorkout;
import com.daiken.workoutprogress.models.ProgramWorkoutGroup;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProgramWorkoutGroupRepository extends MongoRepository<ProgramWorkoutGroup, String> {

    List<ProgramWorkoutGroup> findAllByProgramWorkout(ProgramWorkout programWorkout);
}
