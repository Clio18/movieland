package com.tteam.movieland.controller;

import com.tteam.movieland.dto.UserDto;
import com.tteam.movieland.exception.UserNotFoundException;
import com.tteam.movieland.security.model.Credentials;
import com.tteam.movieland.security.model.SecuredResponse;
import com.tteam.movieland.security.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UserController {

    private final SecurityService securityService;

    @PostMapping("login")
    protected ResponseEntity<SecuredResponse> login(@RequestBody Credentials credentials) {
        try {
            SecuredResponse securedResponse = securityService.login(credentials);
            return ResponseEntity.ok(securedResponse);
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("registration")
    protected ResponseEntity<UserDto> registration(@RequestBody UserDto userDto) {
        UserDto user = securityService.registration(userDto);
        return ResponseEntity.ok(user);
    }


}
