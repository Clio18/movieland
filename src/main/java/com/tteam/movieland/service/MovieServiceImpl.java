package com.tteam.movieland.service;

import com.tteam.movieland.entity.Movie;
import com.tteam.movieland.exception.GenreNotFoundException;
import com.tteam.movieland.exception.MovieNotFoundException;
import com.tteam.movieland.repository.MovieRepository;
import com.tteam.movieland.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {
    private static final int NUMBER_OF_RANDOM_MOVIES = 3;
    private static final Comparator<Movie> COMPARATOR_BY_RATING = Comparator.comparing(Movie::getRating);

    private final MovieRepository movieRepository;

    @Override
    public List<Movie> getAll() {
        return movieRepository.findAll();
    }

    @Override
    public List<Movie> getThreeRandom() {
        List<Movie> movies = movieRepository.findAll();
        //is it thread safe?
        return Utils.pickNRandomElements(movies, NUMBER_OF_RANDOM_MOVIES);
    }

    @Override
    public List<Movie> getMoviesByGenreId(Long genreId) {
        List<Movie> movies = movieRepository.findByGenres_Id(genreId);
        if (movies.isEmpty()) {
            throw new GenreNotFoundException("Could not find genre by id: " + genreId);
        }
        return movies;
    }

    @Override
    public Movie getById(Long movieId) {
        return movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("Could not find movie by id: " + movieId));
    }

    @Override
    public List<Movie> getAllSortedByRating(String sort) {
        List<Movie> movies = getAll();
        return Utils.performSorting(movies, sort, COMPARATOR_BY_RATING);
    }

    @Override
    public List<Movie> getMoviesByGenreSortedByRating(Long genreId, String sort) {
        List<Movie> movies = getMoviesByGenreId(genreId);
        return Utils.performSorting(movies, sort, COMPARATOR_BY_RATING);
    }
}
