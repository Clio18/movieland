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
    //how to get all movie without request param?
    protected List<Movie> getAll(@RequestParam(value = "rating") String sort) {
        return movieService.getAllSortedByRating(sort);
    }

    @GetMapping("random")
    protected List<Movie> getRandomMovie() {
        return movieService.getThreeRandom();
    }

    @GetMapping(value = "/genre/{genreId}")
    protected List<Movie> getMoviesByGenreId(@PathVariable Long genreId, @RequestParam(value = "rating") String sort) {
        return movieService.getMoviesByGenreSortedByRating(genreId, sort);
    }

    @GetMapping("/{movieId}")
    protected ResponseEntity<MovieDto> getMovieById(@PathVariable Long movieId) {
        Movie movie = movieService.getById(movieId);
        MovieDto movieDto = mapper.entityToDto(movie);
        return ResponseEntity.ok(movieDto);
    }


}
