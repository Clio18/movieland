package com.tteam.movieland.repository;

import com.tteam.movieland.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    Page<Movie> findByGenresId(Pageable p, Long id);

    long count();

    Page<Movie> findAll(Pageable p);
}