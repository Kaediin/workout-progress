package com.daiken.workoutprogress.repository;

import com.daiken.workoutprogress.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findOneByFid(String fid);

    long count();
}
