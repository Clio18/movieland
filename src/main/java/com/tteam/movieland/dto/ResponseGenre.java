package com.tteam.movieland.dto;

import lombok.*;

/**
 * @author Oleksandr Shevchenko
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseGenre {

    private Long id;
    private String name;
}
