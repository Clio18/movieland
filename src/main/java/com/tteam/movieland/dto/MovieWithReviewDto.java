package com.tteam.movieland.dto;

import lombok.*;

import java.util.Set;

@ToString
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class MovieWithReviewDto {
    private Long id;
    private String nameUkr;
    private String nameNative;
    private Integer yearOfRelease;
    private String description;
    private Double price;
    private Double rating;
    private String poster;
    private Set<ReviewDto> reviews;
}
