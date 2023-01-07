package com.tteam.movieland.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tteam.movieland.entity.Country;
import com.tteam.movieland.entity.Genre;
import com.tteam.movieland.entity.Movie;
import com.tteam.movieland.exception.MovieNotFoundException;
import com.tteam.movieland.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Set;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MovieService movieService;

    private Movie movie1;
    private Movie movie2;

    @BeforeEach
    void init(){
        Country usa = Country.builder()
                .countryName("usa")
                .build();
        Country australia = Country.builder()
                .countryName("australia")
                .build();
        Set<Country> countries = Set.of(usa, australia);

        Genre drama = Genre.builder()
                .name("drama")
                .build();
        Genre comedy = Genre.builder()
                .name("comedy")
                .build();
        Set<Genre> genres = Set.of(drama, comedy);

        movie1 = Movie.builder()
                .price(10.0)
                .nameUkr("Matrix")
                .nameNative("Matrix")
                .yearOfRelease(1989)
                .rating(10.0)
                .poster("url/")
                .description("Best movie")
                .countries(countries)
                .genres(genres)
                .build();
        movie2 = Movie.builder()
                .price(12.0)
                .nameUkr("Matrix2")
                .nameNative("Matrix2")
                .yearOfRelease(1999)
                .rating(9.0)
                .poster("url/")
                .description("Good movie")
                .countries(countries)
                .genres(genres)
                .build();
    }

    @Test
    @DisplayName("Test GetAllMovies And Check Status Code, Result Size, Fields, Service Method Calling")
    void testGetAllMovies_AndCheckStatus_Size_Fields_ServiceMethodCalling() throws Exception {
        List<Movie> movies = List.of(movie1, movie2);
        when(movieService.getAll()).thenReturn(movies);
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/api/v1/movies")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nameUkr").value("Matrix"))
                .andExpect(jsonPath("$[0].price").value(10.0))
                .andExpect(jsonPath("$[1].nameUkr").value("Matrix2"))
                .andExpect(jsonPath("$[1].price").value(12.0));
        verify(movieService).getAll();
    }

    @Test
    @DisplayName("Test FindMovieById And Check Status Code")
    void testFindById_AndCheckStatus() throws Exception {
        when(movieService.getById(1L)).thenReturn(movie1);
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/api/v1/movies/{movieId}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nameUkr").value("Matrix"))
                .andExpect(jsonPath("$.price").value(10.0));
        verify(movieService).getById(1L);
    }

    @Test
    @DisplayName("Test FindMovieById If Movie Not Found")
    void testFindById_IfMovieNotFound() throws Exception {
        when(movieService.getById(1L)).thenThrow(MovieNotFoundException.class);
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/api/v1/movies/{movieId}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(movieService).getById(1L);
    }
}