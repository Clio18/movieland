package com.tteam.movieland.service;

import com.tteam.movieland.dto.MovieDto;
import com.tteam.movieland.dto.MovieWithCountriesAndGenresDto;
import com.tteam.movieland.entity.Movie;

import java.util.List;

public interface MovieService {

    List<Movie> getRandom();

    List<Movie> getMoviesByGenreId(Long genreId);

    Movie getById(Long movieId, String currency);

    List<Movie> getAllSortedByRating(String sortingOrder);

    List<Movie> getMoviesByGenreSortedByRating(Long genreId, String sortingOrder);

    MovieWithCountriesAndGenresDto saveMovieWithGenresAndCountries(MovieDto movieDto);

    MovieWithCountriesAndGenresDto updateMovieWithGenresAndCountries(Long movieId, MovieDto movieDto);
}

