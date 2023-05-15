package com.daiken.workoutprogress.services;

import com.daiken.workoutprogress.exceptions.UnauthorizedException;
import com.daiken.workoutprogress.model.CognitoUser;
import com.daiken.workoutprogress.model.User;
import com.daiken.workoutprogress.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@Service
public class UserService {

    private final CognitoService cognitoService;
    private final UserRepository userRepository;

    @Autowired
    public UserService(
            CognitoService cognitoService,
            UserRepository userRepository
    ) {
        this.cognitoService = cognitoService;
        this.userRepository = userRepository;
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findUserByFID(String fid) {
        Optional<CognitoUser> cognitoUser = cognitoService.findUser(fid);

        if (cognitoUser.isEmpty()) {
            return Optional.empty();
        }

        return findUser(cognitoUser.get());
    }

    public Optional<User> findUser(CognitoUser cognitoUser) {
        Optional<String> fid = Optional.ofNullable(cognitoUser.fid);
        if (fid.isEmpty()) {
            return Optional.empty();
        }

        Optional<User> user = userRepository.findOneByFid(fid.get());
        if (user.isEmpty()) {
            // Create a new user
            return Optional.of(updateUser(new User(fid.get())));
        }
        return user;
    }

    public User getContextUser() {
        Authentication auth = getContext().getAuthentication();
        String fid = auth.getDetails().toString();
        return findUserByFID(fid).orElseThrow(() -> new UnauthorizedException("Cannot find user"));
    }
}
