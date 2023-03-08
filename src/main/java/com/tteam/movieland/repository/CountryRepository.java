package com.tteam.movieland.repository;

import com.tteam.movieland.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
    @Query(value = "SELECT countries.id, countries.name FROM countries INNER JOIN movie_country ON countries.id = movie_country.country_id WHERE movie_country.movie_id=?;", nativeQuery = true)
    Set<Country> findAllByMovieId(Long id);
}

