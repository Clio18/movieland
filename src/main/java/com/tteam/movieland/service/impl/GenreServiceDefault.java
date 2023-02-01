package com.tteam.movieland.service.impl;

import com.tteam.movieland.entity.Genre;
import com.tteam.movieland.repository.GenreRepository;
import com.tteam.movieland.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreServiceDefault implements GenreService {

    private final GenreRepository genreRepository;

    @Override
    public List<Genre> getAll() {
        return genreRepository.findAll();
    }
}
