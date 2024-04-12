package com.daiken.workoutprogress.api.graphql;

import com.daiken.workoutprogress.models.User;
import com.daiken.workoutprogress.repositories.UserRepository;
import com.daiken.workoutprogress.services.UserService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Query resolver for User
 */
@Slf4j
@PreAuthorize("isAuthenticated()")
@Component
public class UserQueryResolver implements GraphQLQueryResolver {

    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public UserQueryResolver(UserRepository userRepository,
                             UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    /**
     * Get all users
     *
     * @return List of users
     */
    public List<User> users() {
        return userRepository.findAll();
    }

    /**
     * Get user by id
     * @param id User id
     * @return User
     */
    public User userById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    /**
     * Get current user
     * @return Current user
     */
    public User me() {
        return userService.getContextUser();
    }
}
