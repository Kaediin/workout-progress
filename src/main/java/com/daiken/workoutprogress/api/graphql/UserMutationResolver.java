package com.daiken.workoutprogress.api.graphql;

import com.daiken.workoutprogress.api.graphql.input.BiometricsLogInput;
import com.daiken.workoutprogress.api.graphql.input.UserInput;
import com.daiken.workoutprogress.models.BiometricsLog;
import com.daiken.workoutprogress.models.User;
import com.daiken.workoutprogress.repositories.BiometricsLogRepository;
import com.daiken.workoutprogress.repositories.UserRepository;
import com.daiken.workoutprogress.services.UserService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

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
//        User user = new User(userInput);
        return null;
    }

    public User completeOnboarding() {
        User me = userService.getContextUser();
        me.setOnboardingCompleted(true);
        return userRepository.save(me);
    }

    public User logBiometrics(BiometricsLogInput input) {
        User me = userService.getContextUser();
        BiometricsLog biometricsLog = new BiometricsLog(me, input);
        biometricsLogRepository.save(biometricsLog);
        return me;
    }
}
