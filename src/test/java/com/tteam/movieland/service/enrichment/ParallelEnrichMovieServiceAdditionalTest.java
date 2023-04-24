package com.tteam.movieland.service.enrichment;

import com.tteam.movieland.dto.MovieDto;
import com.tteam.movieland.dto.mapper.MovieMapper;
import com.tteam.movieland.service.CountryService;
import com.tteam.movieland.service.GenreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ParallelEnrichMovieServiceAdditionalTest {
    @Mock
    private CountryService countryService;
    @Mock
    private GenreService genreService;
    @Mock
    private MovieMapper mapper;
    private final ExecutorService executorService = Executors.newFixedThreadPool(2);
    private ParallelEnrichMovieService enrichMovieService;
    private MovieDto movieDto;

    @BeforeEach
    void setUp() {
        enrichMovieService = new ParallelEnrichMovieService(countryService, genreService, executorService, mapper);
        movieDto = new MovieDto();
    }

    @Test
    @DisplayName("Test enrich movie with interrupted thread")
    void testEnrichMovieWithInterruptedThread() {
        executorService.shutdownNow();
        assertThrows(RuntimeException.class, () -> enrichMovieService.enrich(movieDto));
    }
}
