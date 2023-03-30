package com.tteam.movieland.service.enrichment;

import com.tteam.movieland.dto.MovieDto;
import com.tteam.movieland.dto.mapper.MovieMapper;
import com.tteam.movieland.entity.Country;
import com.tteam.movieland.entity.Genre;
import com.tteam.movieland.entity.Movie;
import com.tteam.movieland.service.CountryService;
import com.tteam.movieland.service.GenreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public abstract class EnrichMovieTest {
    @Mock
    private CountryService countryService;
    @Mock
    private GenreService genreService;
    @Mock
    private MovieMapper mapper;
    private ExecutorService executorService;
    private EnrichMovieService enrichMovieService;
    private MovieDto movieDto;
    private Movie expectedMovie;
    private Set<Country> expectedCountries;
    private Set<Genre> expectedGenres;

    @BeforeEach
    void setUp() {
        executorService = Executors.newFixedThreadPool(2);
        enrichMovieService = getEnrichMovieService(countryService, genreService, executorService, mapper);

        expectedCountries = Set.of(new Country(1L, "Ukraine"), new Country(2L, "USA"));
        expectedGenres = Set.of(new Genre(1L, "Action"), new Genre(3L, "Drama"));

        movieDto = new MovieDto();
        movieDto.setId(1L);
        
        expectedMovie = new Movie();
        expectedMovie.setId(1L);
        expectedMovie.setCountries(expectedCountries);
        expectedMovie.setGenres(expectedGenres);
    }

    protected abstract EnrichMovieService getEnrichMovieService(CountryService countryService,
                                                                GenreService genreService,
                                                                ExecutorService executorService,
                                                                MovieMapper mapper);

    @Test
    @DisplayName("Enrich movie with countries and genres")
    void enrichMovieWithCountriesAndGenres() {
        Set<Long> countriesIds = Set.of(1L, 2L);
        Set<Long> genresIds = Set.of(1L, 3L);
        movieDto.setCountriesId(countriesIds);
        movieDto.setGenresId(genresIds);

        when(countryService.findAllById(countriesIds)).thenReturn(expectedCountries);
        when(genreService.findAllById(genresIds)).thenReturn(expectedGenres);
        when(mapper.toMovie(movieDto)).thenReturn(expectedMovie);

        Movie actualMovie = enrichMovieService.enrich(movieDto);
        assertEquals(expectedMovie, actualMovie);
    }

    @Test
    @DisplayName("Enrich movie without countries and genres")
    void enrichMovieWithoutCountriesAndGenres() {
        when(countryService.findAllByMovieId(movieDto.getId())).thenReturn(expectedCountries);
        when(genreService.findAllByMovieId(movieDto.getId())).thenReturn(expectedGenres);
        when(mapper.toMovie(movieDto)).thenReturn(expectedMovie);

        Movie actualMovie = enrichMovieService.enrich(movieDto);
        assertEquals(expectedMovie, actualMovie);
    }

    @Test
    @DisplayName("Enrich movie with interrupted thread")
    void enrichMovieWithInterruptedThread() {
        executorService.shutdownNow();
        assertThrows(RuntimeException.class, () -> enrichMovieService.enrich(movieDto));
    }
}
