package com.tteam.movieland.service;

import com.tteam.movieland.entity.Movie;

import java.util.List;

public interface MovieService {
    List<Movie> getAll();

    List<Movie> getThreeRandom();

    List<Movie> getMoviesByGenreId(Long genreId);

    Movie getById(Long movieId);

    List<Movie> getAllSortedByRating(String sort);

    List<Movie> getMoviesByGenreSortedByRating(Long genreId, String sort);
}

