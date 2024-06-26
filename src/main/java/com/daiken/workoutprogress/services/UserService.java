package com.daiken.workoutprogress.services;

import com.daiken.workoutprogress.exceptions.UnauthorizedException;
import com.daiken.workoutprogress.models.CognitoUser;
import com.daiken.workoutprogress.models.User;
import com.daiken.workoutprogress.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

/**
 * Service for User operations.
 */
@Slf4j
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

    /**
     * Update a user
     *
     * @param user User to update
     * @return Updated user
     */
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Find a user by their FID
     * @param fid FID
     * @return User
     */
    public Optional<User> findUserByFID(String fid) {
        Optional<CognitoUser> cognitoUser = cognitoService.findUser(fid);

        if (cognitoUser.isEmpty()) {
            return Optional.empty();
        }

        return findUser(cognitoUser.get());
    }

    /**
     * Find a user by their CognitoUser
     * @param cognitoUser CognitoUser
     * @return User
     */
    public Optional<User> findUser(CognitoUser cognitoUser) {
        Optional<String> fid = Optional.ofNullable(cognitoUser.getFid());
        if (fid.isEmpty()) {
            return Optional.empty();
        }

        Optional<User> user = userRepository.findOneByFid(fid.get());
        if (user.isEmpty()) {
            log.info("[findUser] User not found, creating new one");
            // Create a new user
            return Optional.of(updateUser(new User(fid.get())));
        }
        return user;
    }

    /**
     * Get the current user from the context
     * @return User
     */
    public User getContextUser() {
        Authentication auth = getContext().getAuthentication();
        String fid = auth.getDetails().toString();
        return findUserByFID(fid).orElseThrow(() -> {
            log.error("[getContextUser] Cannot find user");
            return new UnauthorizedException("Cannot find user");
        });
    }
}
