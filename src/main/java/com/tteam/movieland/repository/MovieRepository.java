package com.tteam.movieland.repository;

import com.tteam.movieland.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    Page<Movie> findByGenresId(Pageable p, Long id);
    @Query(value = "SELECT * FROM movies ORDER BY RANDOM() LIMIT 3", nativeQuery = true)
    List<Movie> findThreeRandomMovies();
    @Query("select m from Movie m")
    Page<Movie> findAll(Pageable p);
}