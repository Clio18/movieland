package com.tteam.movieland.service;

import com.tteam.movieland.entity.Genre;
import com.tteam.movieland.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DefaultGenreService implements GenreService {

    private final GenreRepository genreRepository;

    @Override
    public List<Genre> getAll() {
        return genreRepository.findAll();
    }

    public Set<Genre> findAllById(Set<Long> genresIds) {
        return genreRepository.findAllById(genresIds);
    }

    public Set<Genre> findAllByMovieId(Long id) {
        return genreRepository.findAllByMovieId(id);
    }
}

