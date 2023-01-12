package com.tteam.movieland.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tteam.movieland.dto.UserDto;
import com.tteam.movieland.exception.UserNotFoundException;
import com.tteam.movieland.security.SecurityService;
import com.tteam.movieland.security.model.Credentials;
import com.tteam.movieland.security.model.SecuredResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private SecurityService securityService;

    private Credentials credentials = Credentials.builder()
            .email("ronald.reynolds66@example.com")
            .password("paco")
            .build();
    private SecuredResponse securedResponse = SecuredResponse.builder()
            .nickname("Рональд Рейнольдс")
            .uuid(UUID.fromString("2328f883-436f-4262-9809-ffe0b9627f40"))
            .build();

    private UserDto userDto = UserDto.builder()
            .email("ronald.reynolds66@example.com")
            .password("paco")
            .nickname("Рональд Рейнольдс")
            .build();


    @Test
    @DisplayName("Test Login And Check Status Is Ok")
    void testLogin_CheckStatusIsOk() throws Exception {
        when(securityService.login(credentials)).thenReturn(securedResponse);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(securedResponse)))
                .andExpect(status().isOk());
        verify(securityService).login(any(Credentials.class));
    }

    @Test
    @DisplayName("Test Login And Check Status Bad Request")
    void testLogin_CheckStatusBadRequest() throws Exception {
        when(securityService.login(credentials)).thenThrow(new UserNotFoundException("Bad request"));
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/login")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test Registration And Check Status Is Ok")
    void testRegistration_CheckStatusIsOk() throws Exception {
        when(securityService.registration(userDto)).thenReturn(userDto);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk());
        verify(securityService).registration(any(UserDto.class));
    }

}