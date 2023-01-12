package com.tteam.movieland.security.model;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SecuredResponse {
    private UUID uuid;
    private String nickname;
}
