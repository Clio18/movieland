package com.tteam.movieland.service;

import com.tteam.movieland.entity.Genre;

import java.util.List;
import java.util.Set;

public interface GenreService {
    List<Genre> getAll();
    Set<Genre> findAllById(Set<Long> genresIds);
    Set<Genre> findAllByMovieId(Long id);

}
