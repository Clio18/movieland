package com.tteam.movieland.controller;

import com.tteam.movieland.entity.Movie;
import com.tteam.movieland.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/movies", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping
    //how to get all movie without request param?
    protected List<Movie> getAll(@RequestParam(value = "rating") String sort) {
        List<Movie> movies = movieService.getAll();
        return movieService.perform(movies, sort);
    }

    @GetMapping("random")
    protected List<Movie> getRandomMovie() {
        return movieService.getThreeRandom();
    }

    @GetMapping(value = "/genre/{genreId}")
    protected List<Movie> getMoviesByGenreId(@PathVariable Long genreId, @RequestParam(value = "rating") String sort) {
        List<Movie> moviesByGenre = movieService.getMoviesByGenreId(genreId);
        return movieService.perform(moviesByGenre, sort);
    }

    @GetMapping("/{movieId}")
    protected ResponseEntity<Movie> getMovieById(@PathVariable Long movieId) {
        Movie movie = movieService.getById(movieId);
        return ResponseEntity.ok(movie);
    }


}
