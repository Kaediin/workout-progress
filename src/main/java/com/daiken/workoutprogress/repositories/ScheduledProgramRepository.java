package com.daiken.workoutprogress.repositories;

import com.daiken.workoutprogress.models.ScheduledProgram;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ScheduledProgramRepository extends MongoRepository<ScheduledProgram, String> {

    List<ScheduledProgram> findAllByUserId(String user_id);
}
