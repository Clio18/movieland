package com.tteam.movieland.controller;

import com.tteam.movieland.dto.MovieDto;
import com.tteam.movieland.dto.mapper.EntityMapper;
import com.tteam.movieland.entity.Movie;
import com.tteam.movieland.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/movies", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class MovieController {

    @Autowired
    private EntityMapper mapper;
    private final MovieService movieService;

    @GetMapping
    protected List<MovieDto> getAll(@RequestParam(value = "rating", defaultValue = "") String sortingOrder) {
        List<Movie> sortedMovies = movieService.getAllSortedByRating(sortingOrder);
        return sortedMovies.stream().map(mapper::entityToDto).toList();
    }

    @GetMapping("random")
    protected List<MovieDto> getRandomMovie() {
        List<Movie> randomMovies = movieService.getThreeRandom();
        return randomMovies.stream().map(mapper::entityToDto).toList();
    }

    @GetMapping(value = "/genre/{genreId}")
    protected List<MovieDto> getMoviesByGenreId(@PathVariable Long genreId,
                                                @RequestParam(value = "rating", defaultValue = "") String sortingOrder) {
        List<Movie> sortedMovies = movieService.getMoviesByGenreSortedByRating(genreId, sortingOrder);
        return sortedMovies.stream().map(mapper::entityToDto).toList();
    }

    @GetMapping("/{movieId}")
    protected ResponseEntity<MovieDto> getMovieById(@PathVariable Long movieId,
                                                    @RequestParam(value = "currency", defaultValue = "UAH") String currency) {
        Movie movie = movieService.getById(movieId, currency);
        MovieDto movieDto = mapper.entityToDto(movie);
        return ResponseEntity.ok(movieDto);
    }
}
