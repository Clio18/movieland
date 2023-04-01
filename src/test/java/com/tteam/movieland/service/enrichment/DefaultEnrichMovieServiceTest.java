package com.tteam.movieland.service.enrichment;

import com.tteam.movieland.dto.mapper.MovieMapper;
import com.tteam.movieland.service.CountryService;
import com.tteam.movieland.service.GenreService;

public class DefaultEnrichMovieServiceTest extends EnrichMovieTest {
    @Override
    protected EnrichMovieService getEnrichMovieService(CountryService countryService, GenreService genreService, MovieMapper mapper) {
        return new DefaultEnrichMovieService(countryService, genreService, mapper);
    }
}
