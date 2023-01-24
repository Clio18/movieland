package com.tteam.movieland.service;

import com.tteam.movieland.entity.Movie;
import com.tteam.movieland.exception.MovieNotFoundException;
import com.tteam.movieland.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieServiceDefault implements MovieService {
    private final MovieRepository movieRepository;

    @Override
    public List<Movie> getThreeRandom() {
        return movieRepository.findThreeRandomMovies();
    }

    @Override
    public Movie getById(Long movieId) {
        return movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("Could not find movie by id: " + movieId));
    }

    @Override
    public List<Movie> getMoviesByGenreId(Long genreId) {
        return movieRepository.findByGenresId(PageRequest.of(0, 3), genreId).stream().toList();
    }

    @Override
    public List<Movie> getAllSortedByRating(String sortingOrder) {
        if(sortingOrder.toLowerCase().equalsIgnoreCase(Sort.Direction.DESC.name())) {
            return movieRepository.findAll(PageRequest.of(0, 5, Sort.Direction.DESC, "rating")).stream().toList();
        }else if (sortingOrder.equalsIgnoreCase(Sort.Direction.ASC.name())){
            return movieRepository.findAll(PageRequest.of(0, 5, Sort.Direction.ASC, "rating")).stream().toList();
        }else {
            return movieRepository.findAll(PageRequest.of(0, 5)).stream().toList();
        }
    }

    @Override
    public List<Movie> getMoviesByGenreSortedByRating(Long genreId, String sortingOrder) {
        if(sortingOrder.toLowerCase().equalsIgnoreCase(Sort.Direction.DESC.name())) {
            return movieRepository.findByGenresId(PageRequest.of(0, 3, Sort.Direction.DESC, "rating"), genreId).stream().toList();
        }else if (sortingOrder.equalsIgnoreCase(Sort.Direction.ASC.name())){
            return movieRepository.findByGenresId(PageRequest.of(0, 3, Sort.Direction.ASC, "rating"), genreId).stream().toList();
        }else {
            return movieRepository.findByGenresId(PageRequest.of(0, 3), genreId).stream().toList();
        }
    }
}
