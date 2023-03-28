package com.tteam.movieland.service.enrichment;

import com.tteam.movieland.dto.MovieDto;
import com.tteam.movieland.entity.Movie;

public interface EnrichMovieService {
    Movie enrich(MovieDto movieDto);
}