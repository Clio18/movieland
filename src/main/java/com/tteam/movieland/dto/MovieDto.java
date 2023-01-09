package com.tteam.movieland.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieDto {
    private Long id;
    private String nameUkr;
    private String nameNative;
    private Integer yearOfRelease;
    private String description;
    private Double price;
    private Double rating;
    private String poster;
}
