package com.daiken.workoutprogress.api.graphql;

import com.daiken.workoutprogress.model.User;
import com.daiken.workoutprogress.repository.UserRepository;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserQueryResolver implements GraphQLQueryResolver {

    private final UserRepository userRepository;

    @Autowired
    public UserQueryResolver(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> users() {
        return userRepository.findAll();
    }

    public User userById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    public User userByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}
