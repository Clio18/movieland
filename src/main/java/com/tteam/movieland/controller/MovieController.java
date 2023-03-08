package com.tteam.movieland.controller;

import com.tteam.movieland.dto.MovieDto;
import com.tteam.movieland.dto.MovieWithCountriesAndGenresDto;
import com.tteam.movieland.dto.mapper.MovieMapper;
import com.tteam.movieland.entity.Movie;
import com.tteam.movieland.repository.MovieRepository;
import com.tteam.movieland.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
@RequestMapping(value = "/api/v1/movies", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class MovieController {
    private final MovieRepository movieRepository;

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
    protected ResponseEntity<MovieWithCountriesAndGenresDto> getMovieById(@PathVariable Long movieId, @RequestParam(value = "currency", defaultValue = "UAH") String currency) {
        Movie movie = movieService.getById(movieId, currency);
        MovieWithCountriesAndGenresDto movieDto = mapper.toWithCountriesAndGenresDto(movie);
        log.info("Get movie with id: {}", movieId);
        return ResponseEntity.ok(movieDto);
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ADMIN')")
    protected ResponseEntity<MovieWithCountriesAndGenresDto> save(@RequestBody MovieDto movieDto) {
        MovieWithCountriesAndGenresDto withCountriesAndGenresDto = movieService.saveMovieWithGenresAndCountries(movieDto);
        return ResponseEntity.ok(withCountriesAndGenresDto);
    }

    @PutMapping("/{movieId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    protected ResponseEntity<MovieWithCountriesAndGenresDto> update(@PathVariable Long movieId, @RequestBody MovieDto movieDto) {
        MovieWithCountriesAndGenresDto withCountriesAndGenresDto = movieService.updateMovieWithGenresAndCountries(movieId, movieDto);
        return ResponseEntity.ok(withCountriesAndGenresDto);
    }


}
