package com.tteam.movieland.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tteam.movieland.exception.UserNotFoundException;
import com.tteam.movieland.security.model.AuthRequest;
import com.tteam.movieland.security.model.AuthResponse;
import com.tteam.movieland.security.model.RegisterRequest;
import com.tteam.movieland.security.service.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = SpringSecurityTestConfig.class)
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AuthService authService;

    private AuthRequest authRequest = AuthRequest.builder()
            .email("admin@gmail.com")
            .password("password")
            .build();
    private AuthResponse authResponse = AuthResponse.builder()
            .nickname("admin")
            .token("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyb25hbGQucmV5bm9sZHM2NkBleGFtcGxlLmNvbSIsImlhdCI6MTY3MzcxMzI1NiwiZXhwIjoxNjczNzE0Njk2fQ.CiuRcJLXb4OFeguJ007XDWxkQDkEYZy5984jPYz-iJk")
            .build();

    private RegisterRequest registerRequest = RegisterRequest.builder()
            .email("admin@gmail.com")
            .password("password")
            .nickname("admin")
            .build();


    @Test
    @WithUserDetails("user@gmail.com")
    @DisplayName("Test Authenticate And Check Status Is Ok")
    void testAuthenticate_CheckStatusIsOk() throws Exception {
        when(authService.authenticate(authRequest)).thenReturn(authResponse);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authResponse)))
                .andExpect(status().isOk());
        verify(authService).authenticate(any(AuthRequest.class));
    }

    @Test
    @WithUserDetails("user@gmail.com")
    @DisplayName("Test Login And Check Status Bad Request")
    void testLogin_CheckStatusBadRequest() throws Exception {
        when(authService.authenticate(authRequest)).thenThrow(new UserNotFoundException("Bad request"));
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails("user@gmail.com")
    @DisplayName("Test Registration And Check Status Is Ok")
    void testRegistration_CheckStatusIsOk() throws Exception {
        when(authService.register(registerRequest)).thenReturn(authResponse);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/auth/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk());
        verify(authService).register(any(RegisterRequest.class));
    }

}