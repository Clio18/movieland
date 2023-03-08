package com.tteam.movieland.service;

import com.tteam.movieland.entity.Country;
import com.tteam.movieland.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class DefaultCountryService implements CountryService {
    private final CountryRepository countryRepository;

    @Override
    public List<Country> getAll() {
        return countryRepository.findAll();
    }

    public Set<Country> findAllById(Set<Long> countriesIds) {
        return new HashSet<>(countryRepository.findAllById(countriesIds));
    }

    public Set<Country> findAllByMovieId(Long id) {
        return countryRepository.findAllByMovieId(id);
    }
}
