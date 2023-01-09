package com.tteam.movieland.service;

import com.tteam.movieland.entity.Movie;

import java.util.List;

public interface MovieService {
    List<Movie> getAll();

    List<Movie> getThreeRandom();

    List<Movie> getMoviesByGenreId(Long genreId);

    List<Movie> perform(List<Movie> movies, String sort);
    Movie getById(Long movieId);
}

