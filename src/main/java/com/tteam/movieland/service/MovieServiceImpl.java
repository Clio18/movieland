package com.tteam.movieland.service;

import com.tteam.movieland.entity.Movie;
import com.tteam.movieland.exception.MovieNotFoundException;
import com.tteam.movieland.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    @Override
    public List<Movie> getAll() {
        return movieRepository.findAll();
    }

    @Override
    public Movie getById(Long movieId) {
        return movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("Could not find movie by id: " + movieId));
    }
}
