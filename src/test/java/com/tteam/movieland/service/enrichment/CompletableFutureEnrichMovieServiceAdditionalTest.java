package com.tteam.movieland.service.enrichment;

import com.tteam.movieland.dto.MovieDto;
import com.tteam.movieland.dto.mapper.MovieMapper;
import com.tteam.movieland.entity.Country;
import com.tteam.movieland.entity.Genre;
import com.tteam.movieland.service.CountryService;
import com.tteam.movieland.service.GenreService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompletableFutureEnrichMovieServiceAdditionalTest {
    @Mock
    private CountryService countryService;
    @Mock
    private GenreService genreService;
    @Mock
    private MovieMapper mapper;
    private final ExecutorService executorService = Executors.newFixedThreadPool(2);
    private CompletableFutureEnrichMovieService enrichMovieService;
    private Set<Country> expectedCountries;
    private Set<Genre> expectedGenres;
    private MovieDto movieDto;

    @BeforeEach
    void setUp() {
        enrichMovieService = new CompletableFutureEnrichMovieService(countryService, genreService, executorService, mapper);
        expectedCountries = Set.of(new Country(1L, "Ukraine"), new Country(2L, "USA"));
        expectedGenres = Set.of(new Genre(1L, "Action"), new Genre(3L, "Drama"));
        movieDto = new MovieDto();
    }

    @Test
    @DisplayName("Test fetching countries by IDs")
    void testFetchCountries() {
        Set<Long> countriesIds = Set.of(1L, 2L, 3L);
        when(countryService.findAllById(countriesIds)).thenReturn(expectedCountries);
        assertTimeoutPreemptively(Duration.ofSeconds(5), () -> {
            CompletableFuture<Set<Country>> result = enrichMovieService.fetchCountries(countriesIds);
            Assertions.assertNotNull(result);
            Assertions.assertEquals(expectedCountries, result.get());
        });
    }

    @Test
    @DisplayName("Test fetching genres by IDs")
    void testFetchGenres() {
        Set<Long> genreIds = Set.of(1L, 2L, 3L);
        when(genreService.findAllById(genreIds)).thenReturn(expectedGenres);
        assertTimeoutPreemptively(Duration.ofSeconds(5), () -> {
            CompletableFuture<Set<Genre>> result = enrichMovieService.fetchGenres(genreIds);
            Assertions.assertNotNull(result);
            Assertions.assertEquals(expectedGenres, result.get());
        });
    }

    @Test
    @DisplayName("Test fetching countries by movie ID")
    void testFetchCountriesByMovieId() {
        Long movieId = 1L;
        when(countryService.findAllByMovieId(movieId)).thenReturn(expectedCountries);
        assertTimeoutPreemptively(Duration.ofSeconds(5), () -> {
            CompletableFuture<Set<Country>> result = enrichMovieService.fetchCountriesByMovieId(movieId);
            Assertions.assertNotNull(result);
            Assertions.assertEquals(expectedCountries, result.get());
        });
    }

    @Test
    @DisplayName("Test fetching genres by movie ID")
    void testFetchGenresByMovieId() {
        Long movieId = 1L;
        when(genreService.findAllByMovieId(movieId)).thenReturn(expectedGenres);
        assertTimeoutPreemptively(Duration.ofSeconds(5), () -> {
            CompletableFuture<Set<Genre>> result = enrichMovieService.fetchGenresByMovieId(movieId);
            Assertions.assertNotNull(result);
            Assertions.assertEquals(expectedGenres, result.get());
        });
    }

    @Test
    @DisplayName("Test enrich movie with interrupted thread")
    void testEnrichMovieWithInterruptedThread() {
        executorService.shutdownNow();
        assertThrows(RuntimeException.class, () -> enrichMovieService.enrich(movieDto));
    }
}