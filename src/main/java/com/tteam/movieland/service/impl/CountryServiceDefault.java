package com.tteam.movieland.service.impl;

import com.tteam.movieland.entity.Country;
import com.tteam.movieland.repository.CountryRepository;
import com.tteam.movieland.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class CountryServiceDefault implements CountryService {
    private final CountryRepository countryRepository;

    @Override
    public List<Country> getAll() {
        return countryRepository.findAll();
    }
}
