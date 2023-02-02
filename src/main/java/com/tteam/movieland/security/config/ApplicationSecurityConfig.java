package com.tteam.movieland.security.config;

import com.tteam.movieland.exception.UserNotFoundException;
import com.tteam.movieland.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationSecurityConfig {

    private final UserRepository userRepository;

    @Bean
    public UserDetailsService userDetailsService(){
         return username -> userRepository.findByEmail(username)
                 .orElseThrow(() -> new UserNotFoundException(String.format("User not found by provided email: %s", username)));
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
         return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
         DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
         authProvider.setUserDetailsService(userDetailsService());
         authProvider.setPasswordEncoder(passwordEncoder());
         return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }


}