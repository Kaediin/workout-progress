package com.daiken.workoutprogress.api.graphql;

import com.daiken.workoutprogress.api.graphql.input.UserInput;
import com.daiken.workoutprogress.model.User;
import com.daiken.workoutprogress.repository.UserRepository;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMutationResolver implements GraphQLMutationResolver {

    private final UserRepository userRepository;

    @Autowired
    public UserMutationResolver(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(UserInput userInput) {
//        User user = new User(userInput);
        return null;
    }
}
