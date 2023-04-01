package com.tteam.movieland.service.enrichment;

import com.tteam.movieland.dto.mapper.MovieMapper;
import com.tteam.movieland.service.CountryService;
import com.tteam.movieland.service.GenreService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class ParallelEnrichMovieServiceTest extends EnrichMovieTest {
    private final ExecutorService executorService = Executors.newFixedThreadPool(2);
    @Override
    protected EnrichMovieService getEnrichMovieService(CountryService countryService, GenreService genreService, MovieMapper mapper) {
        return new ParallelEnrichMovieService(countryService, genreService, executorService, mapper);
    }
}