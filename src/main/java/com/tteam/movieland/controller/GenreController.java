package com.tteam.movieland.controller;

import com.tteam.movieland.entity.Genre;
import com.tteam.movieland.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Oleksandr Shevchenko
 */
@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping("genres")
    public List<Genre> getAllGenres() {
        return genreService.getAll();
    }
}
