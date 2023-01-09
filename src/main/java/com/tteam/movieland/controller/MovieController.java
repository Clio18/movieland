package com.tteam.movieland.controller;

import com.tteam.movieland.entity.Movie;
import com.tteam.movieland.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/movies", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping
    protected List<Movie> getAllMovie() {
        return movieService.getAll();
    }

    @GetMapping("random")
    protected List<Movie> getRandomMovie() {
        return movieService.getThreeRandom();
    }

    @GetMapping(value = "/genre/{genreId}")
    protected List<Movie> getMoviesByGenreId(@PathVariable Long genreId) {
        return movieService.getMoviesByGenreId(genreId);
    }

    @GetMapping("/{movieId}")
    protected ResponseEntity<Movie> getMovieById(@PathVariable Long movieId) {
        Movie movie = movieService.getById(movieId);
        return ResponseEntity.ok(movie);
    }


}
