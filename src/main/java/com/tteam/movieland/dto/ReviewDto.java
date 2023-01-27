package com.tteam.movieland.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDto {
    private Long id;
    private String content;
    private Long movieId;
    private String userNickname;
}
