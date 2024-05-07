package com.daiken.workoutprogress.repositories;

import com.daiken.workoutprogress.models.ProgramLog;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProgramLogRepository extends MongoRepository<ProgramLog, String> {

    List<ProgramLog> findAllByProgramLogGroupId(String programLogGroup_id);

    List<ProgramLog> findAllByProgramId(String program_id);
}
