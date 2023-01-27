package com.tteam.movieland.security.service;

import com.tteam.movieland.entity.User;
import com.tteam.movieland.exception.UserNotFoundException;
import com.tteam.movieland.security.model.AuthRequest;
import com.tteam.movieland.security.model.AuthResponse;
import com.tteam.movieland.security.model.RegisterRequest;
import com.tteam.movieland.security.model.Role;
import com.tteam.movieland.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        User user = User.builder()
                .email(request.getEmail())
                .nickname(request.getNickname())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.save(user);
        log.info("User {} successfully registered", user.getEmail());
        String token = jwtService.generateToken(user);
        return AuthResponse.builder()
                .nickname(request.getNickname())
                .token(token)
                .build();
    }

    public AuthResponse authenticate(AuthRequest request) {
        String email = request.getEmail();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email,
                        request.getPassword()
                )
        );
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException(String.format("User not found by provided email: %s", email)));
        String token = jwtService.generateToken(user);
        MDC.put("userEmail", user.getEmail());
        MDC.put("userUUID", token);
        log.info("Successful signing up for user {}", user.getEmail());
        return AuthResponse.builder()
                .nickname(user.getNickname())
                .token(token)
                .build();
    }
}
