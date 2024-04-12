package com.daiken.workoutprogress.api.graphql;

import com.daiken.workoutprogress.api.graphql.input.BiometricsLogInput;
import com.daiken.workoutprogress.api.graphql.input.UserInput;
import com.daiken.workoutprogress.models.BiometricsLog;
import com.daiken.workoutprogress.models.User;
import com.daiken.workoutprogress.repositories.BiometricsLogRepository;
import com.daiken.workoutprogress.repositories.UserRepository;
import com.daiken.workoutprogress.services.UserService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * Mutation resolver for User
 */
@Slf4j
@PreAuthorize("isAuthenticated()")
@Component
public class UserMutationResolver implements GraphQLMutationResolver {

    private final BiometricsLogRepository biometricsLogRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public UserMutationResolver(BiometricsLogRepository biometricsLogRepository, UserRepository userRepository, UserService userService) {
        this.biometricsLogRepository = biometricsLogRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public User createUser(UserInput userInput) {
        // This function serves as a placeholder for the schema.graphqls,
        // since that needs at least 1 mutation for it to be valid
        return null;
    }

    /**
     * Complete onboarding
     *
     * @return Updated user
     */
    public User completeOnboarding() {
        User me = userService.getContextUser();
        me.setOnboardingCompleted(true);
        return userRepository.save(me);
    }

    /**
     * Log biometrics
     * @param input Biometrics log input
     * @return Updated user
     */
    public User logBiometrics(BiometricsLogInput input) {
        User me = userService.getContextUser();
        BiometricsLog biometricsLog = new BiometricsLog(me, input);
        biometricsLogRepository.save(biometricsLog);
        return me;
    }
}
