package com.tteam.movieland.security;

import com.tteam.movieland.dto.UserDto;
import com.tteam.movieland.dto.mapper.EntityMapper;
import com.tteam.movieland.entity.User;
import com.tteam.movieland.exception.UserNotFoundException;
import com.tteam.movieland.repository.UserRepository;
import com.tteam.movieland.security.model.Credentials;
import com.tteam.movieland.security.model.SecuredResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecurityServiceImplTest {

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;
    @Mock
    private EntityMapper mapper;
    @InjectMocks
    private SecurityServiceImpl securityService;

    private String email = "ronald.reynolds66@example.com";
    private String password = "paco";
    private String passwordEncoded = "$2a$10$nGg/r1NP6l6Os.F7ZZ056ug0vO9YY0W6PKSYHXF7Ak2yZgkdHJ86q";

    private Credentials credentials = Credentials.builder()
            .email(email)
            .password(password)
            .build();

    private User user = User.builder()
            .email(email)
            .password(passwordEncoded)
            .id(1L)
            .nickname("Рональд Рейнольдс")
            .build();

    private UserDto userDto = UserDto.builder()
            .email(email)
            .password(passwordEncoded)
            .nickname("Рональд Рейнольдс")
            .build();

    @Test
    @DisplayName("Test Login With Valid Request And Check Response Is Not Null, Content And Method Calling")
    void testLoginWithValidRequest_CheckResponseNotNull_Content_MethodCalling() {
        when(passwordEncoder.matches(password, passwordEncoded)).thenReturn(true);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(mapper.entityToDto(user)).thenReturn(userDto);
        SecuredResponse response = securityService.login(credentials);
        assertNotNull(response);
        assertEquals(user.getNickname(), response.getNickname());
        assertNotNull(response.getUuid());
        verify(userRepository).findByEmail(email);
    }

    @Test
    @DisplayName("Test Login With Invalid Email And Check Exception Throwing")
    void testLoginWithInValidEmail_CheckExceptionThrowing() {
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            securityService.login(credentials);
        });
        assertEquals(String.format("User not found by provided email: %s", email), exception.getMessage());
    };

    @Test
    @DisplayName("Test Login With Invalid Password And Check Exception Throwing")
    void testLoginWithInValidPassword_CheckExceptionThrowing() {
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, passwordEncoded)).thenReturn(false);
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            securityService.login(credentials);
        });
        assertEquals(String.format("Incorrect password: %s", credentials.getPassword()), exception.getMessage());
    };

    @Test
    @DisplayName("Test Login With Valid Request And Check One Call To Db")
    void testLoginWithValidRequest_CheckOneCallToDb() {
        when(passwordEncoder.matches(password, passwordEncoded)).thenReturn(true);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(mapper.entityToDto(user)).thenReturn(userDto);
        securityService.login(credentials);
        securityService.login(credentials);
        securityService.login(credentials);
        verify(userRepository).findByEmail(email);
    }

    @Test
    @DisplayName("Test Registration And Check Not Null And Method Calling")
    void testRegistration_CheckNotNull_MethodCalling(){
        when(mapper.dtoToEntity(userDto)).thenReturn(user);
        UserDto dto = securityService.registration(userDto);
        assertNotNull(dto);
        verify(userRepository).save(user);
    }

}