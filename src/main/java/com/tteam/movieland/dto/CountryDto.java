package com.tteam.movieland.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CountryDto {
    private Long id;

    @JsonProperty
    private String name;
}

