package com.daiken.workoutprogress.api.graphql;

import com.daiken.workoutprogress.models.CognitoUser;
import com.daiken.workoutprogress.models.User;
import com.daiken.workoutprogress.services.CognitoService;
import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserResolver implements GraphQLResolver<User> {

    private final CognitoService cognitoService;

    @Autowired
    public UserResolver(CognitoService cognitoService) {
        this.cognitoService = cognitoService;
    }

    public CognitoUser cognitoUser(User user) {
        CognitoUser cognitoUser = cognitoService.findUser(user.getFid()).orElse(null);
        return cognitoUser;
    }
}
