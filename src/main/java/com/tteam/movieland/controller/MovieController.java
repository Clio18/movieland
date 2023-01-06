package com.tteam.movieland.controller;

import com.tteam.movieland.entity.Movie;
import com.tteam.movieland.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping("movies")
    protected @ResponseBody List<Movie> getAllMovie() {
        return movieService.getAll();
    }

    @GetMapping("movies/{movieId}")
    protected ResponseEntity<Movie> getMovieByIdShort(@PathVariable("movieId") Long movieId) {
        return ResponseEntity.ok(movieService.getById(movieId));
    }


}
