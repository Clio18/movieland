package com.tteam.movieland.service.enrichment;

import com.tteam.movieland.dto.mapper.MovieMapper;
import com.tteam.movieland.service.CountryService;
import com.tteam.movieland.service.GenreService;

import java.util.concurrent.ExecutorService;

class ParallelEnrichMovieServiceTest extends EnrichMovieTest {
    @Override
    protected EnrichMovieService getEnrichMovieService(CountryService countryService, GenreService genreService, ExecutorService executorService, MovieMapper mapper) {
        return new ParallelEnrichMovieService(countryService, genreService, executorService, mapper);
    }
}