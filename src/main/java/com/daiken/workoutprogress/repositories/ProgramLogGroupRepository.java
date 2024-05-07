package com.daiken.workoutprogress.repositories;

import com.daiken.workoutprogress.models.ProgramLogGroup;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProgramLogGroupRepository extends MongoRepository<ProgramLogGroup, String> {

    List<ProgramLogGroup> findAllByProgramId(String programId);
}
