package com.tteam.movieland.service;

import com.tteam.movieland.dto.mapper.EntityMapper;
import com.tteam.movieland.entity.Movie;
import com.tteam.movieland.exception.GenreNotFoundException;
import com.tteam.movieland.exception.MovieNotFoundException;
import com.tteam.movieland.repository.MovieRepository;
import com.tteam.movieland.util.CurrencyProvider;
import com.tteam.movieland.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {
    private static final int NUMBER_OF_RANDOM_MOVIES = 3;
    private static final Comparator<Movie> COMPARATOR_BY_RATING = Comparator.comparing(Movie::getRating);

    private final MovieRepository movieRepository;

    @Autowired
    private EntityMapper mapper;

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
        List<Movie> moviesByGenre = movieRepository.findByGenres_Id(genreId);
        if (moviesByGenre.isEmpty()) {
            throw new GenreNotFoundException("Could not find genre by id: " + genreId);
        }
        return moviesByGenre;
    }

    @Override
    public Movie getById(Long movieId, String currency) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("Could not find movie by id: " + movieId));
        return CurrencyProvider.setPriceInCurrency(movie, currency);
    }

    @Override
    public List<Movie> getAllSortedByRating(String sortingOrder) {
        List<Movie> movies = getAll();
        return Utils.performSorting(movies, sortingOrder, COMPARATOR_BY_RATING);
    }

    @Override
    public List<Movie> getMoviesByGenreSortedByRating(Long genreId, String sortingOrder) {
        List<Movie> movies = getMoviesByGenreId(genreId);
        return Utils.performSorting(movies, sortingOrder, COMPARATOR_BY_RATING);
    }
}
