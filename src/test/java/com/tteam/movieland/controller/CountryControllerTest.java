package com.tteam.movieland.controller;

import com.tteam.movieland.dto.CountryDto;
import com.tteam.movieland.dto.mapper.CountryMapper;
import com.tteam.movieland.entity.Country;
import com.tteam.movieland.security.SpringSecurityTestConfig;
import com.tteam.movieland.service.CountryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = SpringSecurityTestConfig.class)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class CountryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CountryService countryService;

    @MockBean
    private CountryMapper entityMapper;

    private List<Country> countryList;
    private Country country1;
    private Country country2;
    private CountryDto countryDto1;
    private CountryDto countryDto2;

    @BeforeEach
    void init() {
        country1 = Country.builder()
                .countryName("ukraine")
                .build();
        country2 = Country.builder()
                .countryName("usa")
                .build();
        countryList = Arrays.asList(country1, country2);
        countryDto1 = CountryDto.builder()
                .countryName("ukraine")
                .build();
        countryDto2 = CountryDto.builder()
                .countryName("usa")
                .build();
    }

    @Test
    @WithUserDetails("user@gmail.com")
    @DisplayName("Test GetAll And Check Status Code, Result Size, Fields, Service Method Calling")
    void testGetAllAndCheckStatusSizeFieldsServiceMethodCalling() throws Exception {
        when(countryService.getAll()).thenReturn(countryList);
        when(entityMapper.toCountryDto(country1)).thenReturn(countryDto1);
        when(entityMapper.toCountryDto(country2)).thenReturn(countryDto2);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/country")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("ukraine"))
                .andExpect(jsonPath("$[1].name").value("usa"));
        verify(countryService).getAll();
        verify(entityMapper, times(2)).toCountryDto(isA(Country.class));
    }

}