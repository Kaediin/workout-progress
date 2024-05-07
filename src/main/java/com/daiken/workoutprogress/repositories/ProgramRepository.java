package com.daiken.workoutprogress.repositories;

import com.daiken.workoutprogress.models.Program;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ProgramRepository extends MongoRepository<Program, String> {

    List<Program> findAllByUserId(String user_id);

    Optional<Program> findByIdAndUserId(String id, String user_id);
}
