package com.tteam.movieland.repository;

import com.tteam.movieland.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface JpaGenreRepository extends JpaRepository<Genre, Long> {
    @Query(value = "SELECT genres.id, genres.name FROM genres INNER JOIN movie_genre ON genres.id=movie_genre.genre_id WHERE movie_genre.movie_id=?;", nativeQuery = true)
    Set<Genre> findAllByMovieId(Long id);
}