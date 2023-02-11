package com.tteam.movieland.repository;

import com.tteam.movieland.entity.Genre;

import java.util.List;

public interface GenreRepository {
    List<Genre> findAll();
}
