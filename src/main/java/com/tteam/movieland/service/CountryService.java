package com.tteam.movieland.service;

import com.tteam.movieland.entity.Country;

import java.util.List;
import java.util.Set;

public interface CountryService {
    List<Country> getAll();
    Set<Country> findAllById(Set<Long> countriesIds);
    Set<Country> findAllByMovieId(Long id);
}
