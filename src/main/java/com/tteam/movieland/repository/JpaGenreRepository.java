package com.tteam.movieland.repository;

import com.tteam.movieland.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaGenreRepository extends JpaRepository<Genre, Long> {
}