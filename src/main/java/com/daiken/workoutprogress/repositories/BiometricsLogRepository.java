package com.daiken.workoutprogress.repositories;

import com.daiken.workoutprogress.models.BiometricsLog;
import com.daiken.workoutprogress.models.BiometricsType;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface BiometricsLogRepository extends MongoRepository<BiometricsLog, String> {
    // Find the latest weight by user
    @Aggregation(pipeline = {
            "{ '$match': {$and: [" +
                    "{ 'user.id': ?0}," +
                    "{ 'type': ?1 }," +
                    "]}" +
                    "}",
            "{ '$sort' : { 'logDate' : -1 } }",
            "{ '$limit' : 1 }"
    })
    Optional<BiometricsLog> findLatestByUserAndType(String user_id, BiometricsType type);
}
