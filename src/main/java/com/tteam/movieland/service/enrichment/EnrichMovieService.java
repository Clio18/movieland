package com.tteam.movieland.service.enrichment;

import com.tteam.movieland.entity.Movie;

public interface EnrichMovieService {
    void enrich(Movie movie);
}