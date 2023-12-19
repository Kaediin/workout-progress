package com.daiken.workoutprogress.repositories;

import com.daiken.workoutprogress.models.Preference;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PreferenceRepository extends MongoRepository<Preference, String> {

    Optional<Preference> findByUserId(String user_id);

}
