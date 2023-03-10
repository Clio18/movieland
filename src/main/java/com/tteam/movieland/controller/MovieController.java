package com.tteam.movieland.controller;

import com.tteam.movieland.dto.MovieDto;
import com.tteam.movieland.dto.mapper.MovieMapper;
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

    private final MovieMapper mapper;
    private final MovieService movieService;

    @GetMapping
    protected List<MovieDto> getAllSortedByRating(@RequestParam(value = "rating", defaultValue = "") String sortingOrder) {
        List<Movie> sortedMovies = movieService.getAllSortedByRating(sortingOrder);
        return sortedMovies.stream().map(mapper::toMovieDto).toList();
    }

    @GetMapping("random")
    protected List<MovieDto> getRandomMovie() {
        List<Movie> randomMovies = movieService.getRandom();
        return randomMovies.stream().map(mapper::toMovieDto).toList();
    }

    @GetMapping(value = "/genre/{genreId}")
    protected List<MovieDto> getMoviesByGenreId(@PathVariable Long genreId,
                                                @RequestParam(value = "rating", defaultValue = "") String sortingOrder) {
        List<Movie> sortedMovies = movieService.getMoviesByGenreSortedByRating(genreId, sortingOrder);
        return sortedMovies.stream().map(mapper::toMovieDto).toList();
    }

    @GetMapping("/{movieId}")
    protected ResponseEntity<MovieDto> getMovieById(@PathVariable Long movieId) {
        Movie movie = movieService.getById(movieId);
        MovieDto movieDto = mapper.toMovieDto(movie);
        return ResponseEntity.ok(movieDto);
    }
}
