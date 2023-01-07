package com.tteam.movieland.controller;

import com.tteam.movieland.entity.Movie;
import com.tteam.movieland.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/movies", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping
    protected @ResponseBody List<Movie> getAllMovie() {
        return movieService.getAll();
    }

    @GetMapping("random")
    protected @ResponseBody List<Movie> getRandomMovie() {
        return movieService.getThreeRandom();
    }


}
