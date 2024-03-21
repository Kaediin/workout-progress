package com.daiken.workoutprogress.api.graphql;

import com.daiken.workoutprogress.models.User;
import com.daiken.workoutprogress.repositories.UserRepository;
import com.daiken.workoutprogress.services.UserService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.List;

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

    public List<User> users() {
        return userRepository.findAll();
    }

    public User userById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    public User me() {
        return userService.getContextUser();
    }
}
