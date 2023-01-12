package com.tteam.movieland.security;

import com.tteam.movieland.dto.UserDto;
import com.tteam.movieland.security.model.Credentials;
import com.tteam.movieland.security.model.SecuredResponse;

public interface SecurityService {
    SecuredResponse login(Credentials credentials);

    UserDto registration(UserDto userDto);
}
