package com.tteam.movieland.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GenreDto {
    @JsonIgnore
    private Long id;
    private String name;
}
