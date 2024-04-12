package com.daiken.workoutprogress.api.graphql;

import com.daiken.workoutprogress.models.BiometricsLog;
import com.daiken.workoutprogress.models.BiometricsType;
import com.daiken.workoutprogress.models.CognitoUser;
import com.daiken.workoutprogress.models.User;
import com.daiken.workoutprogress.repositories.BiometricsLogRepository;
import com.daiken.workoutprogress.services.CognitoService;
import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * Resolver for User
 */
@PreAuthorize("isAuthenticated()")
@Component
public class UserResolver implements GraphQLResolver<User> {

    private final BiometricsLogRepository biometricsLogRepository;
    private final CognitoService cognitoService;

    @Autowired
    public UserResolver(
            BiometricsLogRepository biometricsLogRepository,
            CognitoService cognitoService) {
        this.biometricsLogRepository = biometricsLogRepository;
        this.cognitoService = cognitoService;
    }

    /**
     * Get cognito user
     *
     * @param user User
     * @return Cognito user
     */
    public CognitoUser cognitoUser(User user) {
        return cognitoService.findUser(user.getFid()).orElse(null);
    }

    /**
     * Get latest weight biometrics log
     * @param user User
     * @return Latest weight biometrics log
     */
    public BiometricsLog weight(User user) {
        return biometricsLogRepository.findLatestByUserAndType(user.getId(), BiometricsType.WEIGHT).orElse(null);
    }
}
