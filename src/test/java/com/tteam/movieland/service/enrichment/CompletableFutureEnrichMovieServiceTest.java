package com.tteam.movieland.service.enrichment;

import com.tteam.movieland.dto.mapper.MovieMapper;
import com.tteam.movieland.service.CountryService;
import com.tteam.movieland.service.GenreService;

import java.util.concurrent.ExecutorService;

class CompletableFutureEnrichMovieServiceTest extends EnrichMovieTest {
    @Override
    protected EnrichMovieService getEnrichMovieService(CountryService countryService, GenreService genreService, ExecutorService executorService, MovieMapper mapper) {
        return new CompletableFutureEnrichMovieService(countryService, genreService, executorService, mapper);
    }
}