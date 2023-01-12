package com.tteam.movieland.service;

import com.tteam.movieland.entity.Movie;

import java.util.List;

public interface MovieService {
    List<Movie> getAll();

    List<Movie> getThreeRandom();

    List<Movie> getMoviesByGenreId(Long genreId);

    Movie getById(Long movieId, String currency);

    List<Movie> getAllSortedByRating(String sortingOrder);

    List<Movie> getMoviesByGenreSortedByRating(Long genreId, String sortingOrder);
}

