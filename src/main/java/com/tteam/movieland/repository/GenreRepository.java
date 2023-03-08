package com.tteam.movieland.repository;

import com.tteam.movieland.entity.Genre;
import java.util.List;
import java.util.Set;

public interface GenreRepository {
    List<Genre> findAll();

    Set<Genre> findAllById(Set<Long> genresIds);

    Set<Genre> findAllByMovieId(Long id);

}

