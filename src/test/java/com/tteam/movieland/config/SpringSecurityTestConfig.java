package com.tteam.movieland.config;

import com.tteam.movieland.entity.User;
import com.tteam.movieland.security.model.Role;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Arrays;
import java.util.Set;

@TestConfiguration
public class SpringSecurityTestConfig {

    @Bean
    @Primary
    public UserDetailsService defaultUserDetailsService() {
        User admin = User.builder()
                .nickname("admin")
                .role(Role.ADMIN)
                .email("admin@gmail.com")
                .password("password")
                .grantedAuthorities(Set.of(new SimpleGrantedAuthority(Role.ADMIN.name())))
                .isEnabled(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .build();
        User user = User.builder()
                .nickname("user")
                .email("user@gmail.com")
                .password("password")
                .role(Role.USER)
                .grantedAuthorities(Set.of(new SimpleGrantedAuthority(Role.USER.name())))
                .isEnabled(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .build();

        return new InMemoryUserDetailsManager(Arrays.asList(
                admin, user
        ));
    }

}
