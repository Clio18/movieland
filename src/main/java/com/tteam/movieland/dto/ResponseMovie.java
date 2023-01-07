package com.tteam.movieland.dto;

import com.tteam.movieland.entity.Country;
import com.tteam.movieland.entity.Genre;
import lombok.*;

import java.util.Set;

/**
 * @author Oleksandr Shevchenko
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseMovie {

    private Long id;
    private String nameUkr;
    private String nameNative;
    private Integer yearOfRelease;
    private String description;
    private Double price;
    private Double rating;
    private Set<Country> countries;
    private String poster;
    private Set<Genre> genres;
}
