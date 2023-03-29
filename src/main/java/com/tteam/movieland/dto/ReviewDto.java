package com.tteam.movieland.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDto {
    @JsonIgnore
    private Long id;
    private String content;
    private Long movieId;
    private String userNickname;
}
