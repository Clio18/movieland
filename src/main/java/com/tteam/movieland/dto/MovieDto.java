package com.tteam.movieland.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Set;

@ToString
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class MovieDto {
    @JsonIgnore
    private Long id;
    private String nameUkr;
    private String nameNative;
    private Integer yearOfRelease;
    private String description;
    private Double price;
    private Double rating;
    @JsonProperty("picturePath")
    private String poster;
    @JsonProperty("countries")
    private Set<Long> countriesId;
    @JsonProperty("genres")
    private Set<Long> genresId;
}
