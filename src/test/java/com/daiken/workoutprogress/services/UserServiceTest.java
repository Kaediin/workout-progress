package com.daiken.workoutprogress.services;

import com.daiken.workoutprogress.exceptions.UnauthorizedException;
import com.daiken.workoutprogress.models.CognitoUser;
import com.daiken.workoutprogress.models.User;
import com.daiken.workoutprogress.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CognitoService cognitoService;

    @InjectMocks
    private UserService userService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    @Test
    void updateUserShouldSaveUser() {
        User user = new User("fid-1234");
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.updateUser(user);

        assertEquals("fid-1234", result.getFid());
        verify(userRepository).save(user);
    }

    @Test
    void findUserByFIDShouldReturnEmptyWhenUserNotFound() {
        when(cognitoService.findUser("non-existing-fid")).thenReturn(Optional.empty());

        Optional<User> result = userService.findUserByFID("non-existing-fid");

        assertTrue(result.isEmpty());
        verify(cognitoService).findUser("non-existing-fid");
    }

    @Test
    void findUserShouldReturnUserWhenFIDExists() {
        CognitoUser cognitoUser = new CognitoUser();
        cognitoUser.setFid("existing-fid");
        when(userRepository.findOneByFid("existing-fid")).thenReturn(Optional.of(new User("existing-fid")));

        Optional<User> result = userService.findUser(cognitoUser);

        assertTrue(result.isPresent());
        assertEquals("existing-fid", result.get().getFid());
    }

    @Test
    void getContextUserShouldThrowUnauthorizedWhenUserNotFound() {
        when(authentication.getDetails()).thenReturn("non-existing-fid");
        when(cognitoService.findUser("non-existing-fid")).thenReturn(Optional.empty());

        assertThrows(UnauthorizedException.class, () -> userService.getContextUser());

        verify(authentication).getDetails();
        verify(cognitoService).findUser("non-existing-fid");
    }
}
