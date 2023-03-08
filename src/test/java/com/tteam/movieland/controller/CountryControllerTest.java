package com.tteam.movieland.controller;

import com.tteam.movieland.AbstractBaseITest;
import com.tteam.movieland.dto.CountryDto;
import com.tteam.movieland.dto.mapper.CountryMapper;
import com.tteam.movieland.entity.Country;
import com.tteam.movieland.service.CountryService;
import com.tteam.movieland.service.DefaultCountryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class CountryControllerTest extends AbstractBaseITest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DefaultCountryService countryService;

    @MockBean
    private CountryMapper mapper;

    private List<Country> countryList;
    private Country country1;
    private Country country2;
    private CountryDto countryDto1;
    private CountryDto countryDto2;

    @BeforeEach
    void init() {
        country1 = Country.builder()
                .name("ukraine")
                .build();
        country2 = Country.builder()
                .name("usa")
                .build();
        countryList = Arrays.asList(country1, country2);
        countryDto1 = CountryDto.builder()
                .name("ukraine")
                .build();
        countryDto2 = CountryDto.builder()
                .name("usa")
                .build();
    }

    @Test
    @WithMockUser
    @DisplayName("Test GetAll And Check Status Code, Result Size, Fields, Service Method Calling")
    void testGetAllAndCheckStatusSizeFieldsServiceMethodCalling() throws Exception {
        when(countryService.getAll()).thenReturn(countryList);
        when(mapper.toCountryDto(country1)).thenReturn(countryDto1);
        when(mapper.toCountryDto(country2)).thenReturn(countryDto2);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/country")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("ukraine"))
                .andExpect(jsonPath("$[1].name").value("usa"));
        verify(countryService).getAll();
        verify(mapper, times(2)).toCountryDto(isA(Country.class));
    }

}