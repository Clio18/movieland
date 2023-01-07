package com.tteam.movieland.service;

import com.tteam.movieland.entity.Movie;
import com.tteam.movieland.repository.MovieRepository;
import com.tteam.movieland.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {
    private static final int NUMBER_OF_RANDOM_MOVIES = 3;

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
}
