package com.daiken.workoutprogress.repositories;

import com.daiken.workoutprogress.models.ProgramWorkoutGroup;
import com.daiken.workoutprogress.models.ProgramWorkoutLog;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProgramWorkoutLogRepository extends MongoRepository<ProgramWorkoutLog, String> {

    List<ProgramWorkoutLog> findAllByProgramWorkoutGroup(ProgramWorkoutGroup programWorkoutGroup);
}
