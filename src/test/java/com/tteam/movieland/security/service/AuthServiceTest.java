package com.tteam.movieland.security.service;

import com.tteam.movieland.entity.User;
import com.tteam.movieland.exception.UserNotFoundException;
import com.tteam.movieland.repository.UserRepository;
import com.tteam.movieland.security.model.AuthRequest;
import com.tteam.movieland.security.model.AuthResponse;
import com.tteam.movieland.security.model.RegisterRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    private final String email = "ronald.reynolds66@example.com";
    private final String password = "paco";
    private final String passwordEncoded = "$2a$10$nGg/r1NP6l6Os.F7ZZ056ug0vO9YY0W6PKSYHXF7Ak2yZgkdHJ86q";
    private final String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyb25hbGQucmV5bm9sZHM2NkBleGFtcGxlLmNvbSIsImlhdCI6MTY3MzcxMzI1NiwiZXhwIjoxNjczNzE0Njk2fQ.CiuRcJLXb4OFeguJ007XDWxkQDkEYZy5984jPYz-iJk";

    private final AuthRequest authRequest = AuthRequest.builder()
            .email(email)
            .password(password)
            .build();

    private final User user = User.builder()
            .email(email)
            .password(password)
            .id(1L)
            .nickname("Рональд Рейнольдс")
            .build();

    private final RegisterRequest registerRequest = RegisterRequest.builder()
            .email(email)
            .password(password)
            .nickname("Рональд Рейнольдс")
            .build();

    @Test
    @DisplayName("Test Login With Valid Request And Check Response Is Not Null, Content And Method Calling")
    void testLoginWithValidRequest_CheckResponseNotNull_Content_MethodCalling() {
        when(userRepository.findByEmail(authRequest.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn(token);

        AuthResponse authResponse = authService.authenticate(authRequest);

        assertNotNull(authResponse);
        assertEquals(user.getNickname(), authResponse.getNickname());
        assertEquals(token, authResponse.getToken());

        verify(userRepository).findByEmail(authRequest.getEmail());
        verify(jwtService).generateToken(user);
    }

    @Test
    @DisplayName("Test Login With Invalid Email And Check Exception Throwing")
    void testLoginWithInValidEmail_CheckExceptionThrowing() {
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            authService.authenticate(authRequest);
        });
        assertEquals(String.format("User not found by provided email: %s", email), exception.getMessage());
    }

    ;

    @Test
    @DisplayName("Test Login With Invalid Password And Check Exception Throwing")
    void testLoginWithInValidPassword_CheckExceptionThrowing() {
        lenient().when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getPassword(),
                        authRequest.getEmail()))).thenReturn(null);
        assertThrows(UserNotFoundException.class, () -> {
            authService.authenticate(authRequest);
        });
    }

    ;

    @Test
    @DisplayName("Test Registration And Check Not Null And Method Calling")
    void testRegistration_CheckNotNull_MethodCalling() {
        AuthResponse authResponse = authService.register(registerRequest);

        assertNotNull(authResponse);
        assertEquals(user.getNickname(), authResponse.getNickname());

        verify(userRepository).save(any(User.class));
        verify(jwtService).generateToken(any(User.class));
    }

}