package com.tteam.movieland.controller;

import com.tteam.movieland.dto.CountryDto;
import com.tteam.movieland.dto.mapper.CountryMapper;
import com.tteam.movieland.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class CountryController {

    private final CountryService countryService;
    private final CountryMapper entityMapper;

    @GetMapping("country")
    protected List<CountryDto> getAll(){
        return countryService.getAll().stream().map(entityMapper::toCountryDto).collect(Collectors.toList());
    }
}
