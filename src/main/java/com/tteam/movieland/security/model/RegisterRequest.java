package com.tteam.movieland.security.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
    private String email;
    private String password;
    private String nickname;
}
