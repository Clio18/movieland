package com.tteam.movieland.service;

import com.tteam.movieland.entity.Country;
import com.tteam.movieland.repository.CountryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultCountryServiceTest {
    @Mock
    private CountryRepository countryRepository;

    private List<Country> countryList;
    private CountryService countryService;

    @BeforeEach
    void init() {
        Country country1 = Country.builder()
                .countryName("ukraine")
                .build();
        Country country2 = Country.builder()
                .countryName("usa")
                .build();
        countryList = Arrays.asList(country1, country2);
        countryService = new DefaultCountryService(countryRepository);
    }

    @Test
    @DisplayName("test getAll and check result is not null, size, content equality, calling the repo's method")
    void testGetAllAndCheckResultNotNullSizeContentCallingTheRepositoryMethod() {
        when(countryRepository.findAll()).thenReturn(countryList);
        List<Country> countries = countryService.getAll();
        assertAll(
                () -> assertNotNull(countries),
                () -> assertFalse(countries.isEmpty()),
                () -> assertEquals(2, countries.size()),
                () -> assertArrayEquals(countryList.toArray(), countries.toArray()));
        verify(countryRepository).findAll();
    }


}