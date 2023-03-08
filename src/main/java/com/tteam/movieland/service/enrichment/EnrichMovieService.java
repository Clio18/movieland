package com.tteam.movieland.service.enrichment;

import com.tteam.movieland.dto.MovieDto;
import com.tteam.movieland.entity.Movie;

public interface EnrichMovieService {
    void enrich(MovieDto movieDto, Movie updatedMovie);
    void enrich(Movie updatedMovie);

}