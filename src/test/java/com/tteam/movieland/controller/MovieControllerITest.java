package com.tteam.movieland.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;;
import com.tteam.movieland.AbstractBaseITest;;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DBRider
@AutoConfigureMockMvc(addFilters = false)
class MovieControllerITest extends AbstractBaseITest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Test
    @DataSet(value = "all_dataset.yml", cleanBefore = true, skipCleaningFor = "flyway_schema_history")
    @DisplayName("Test GetAll Without Parameters")
    void testGetAllWithoutParameters() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/movies")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getResponseAsString("response/movies/get-all-without-parameters.json")));
    }

    @Test
    @DataSet(value = "all_dataset.yml", cleanBefore = true, skipCleaningFor = "flyway_schema_history")
    @DisplayName("Test GetAll Rating Descending Order")
    void testGetAllDescRating() throws Exception {
        String sortingOrder = "desc";
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/movies?rating={sortingOrder}", sortingOrder)
                        .header("sortingOrder", sortingOrder)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getResponseAsString("response/movies/get-all-desc-rating.json")));
    }

    @Test
    @DataSet(value = "all_dataset.yml", cleanBefore = true, skipCleaningFor = "flyway_schema_history")
    @DisplayName("Test GetAll Rating Ascending Order")
    void testGetAllAscRating() throws Exception {
        String sortingOrder = "asc";
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/movies?rating={sortingOrder}", sortingOrder)
                        .header("sortingOrder", sortingOrder)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getResponseAsString("response/movies/get-all-asc-rating.json")));
    }

    @Test
    @DataSet(value = "all_dataset.yml", cleanBefore = true, skipCleaningFor = "flyway_schema_history")
    @DisplayName("Test Get three random movies")
    void testGetThreeRandomMovies() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/movies/random")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", notNullValue()))
                .andExpect(jsonPath("$[0].nameUkr", notNullValue()))
                .andExpect(jsonPath("$[0].nameNative", notNullValue()))
                .andExpect(jsonPath("$[0].yearOfRelease", notNullValue()))
                .andExpect(jsonPath("$[0].description", notNullValue()))
                .andExpect(jsonPath("$[0].price", notNullValue()))
                .andExpect(jsonPath("$[0].rating", notNullValue()))
                .andExpect(jsonPath("$[0].picturePath", notNullValue()))

                .andExpect(jsonPath("$[1].id", notNullValue()))
                .andExpect(jsonPath("$[1].nameUkr", notNullValue()))
                .andExpect(jsonPath("$[1].nameNative", notNullValue()))
                .andExpect(jsonPath("$[1].yearOfRelease", notNullValue()))
                .andExpect(jsonPath("$[1].description", notNullValue()))
                .andExpect(jsonPath("$[1].price", notNullValue()))
                .andExpect(jsonPath("$[1].rating", notNullValue()))
                .andExpect(jsonPath("$[1].picturePath", notNullValue()))

                .andExpect(jsonPath("$[2].id", notNullValue()))
                .andExpect(jsonPath("$[2].nameUkr", notNullValue()))
                .andExpect(jsonPath("$[2].nameNative", notNullValue()))
                .andExpect(jsonPath("$[2].yearOfRelease", notNullValue()))
                .andExpect(jsonPath("$[2].description", notNullValue()))
                .andExpect(jsonPath("$[2].price", notNullValue()))
                .andExpect(jsonPath("$[2].rating", notNullValue()))
                .andExpect(jsonPath("$[2].picturePath", notNullValue())
                );
    }

    @Test
    @DataSet(value = "all_dataset.yml", cleanBefore = true, skipCleaningFor = "flyway_schema_history")
    @DisplayName("Test Get Movies By Genre Id Without Parameters")
    void testGetMoviesByGenreIdWithoutParameters() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/movies/genre/{genreId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getResponseAsString("response/movies/get-movies-by-genre-id-without-parameters.json")));
    }

    @Test
    @DataSet(value = "all_dataset.yml", cleanBefore = true, skipCleaningFor = "flyway_schema_history")
    @DisplayName("Test Get Movies By Genre Id And Rating Descending")
    void testGetMoviesByGenreIdAndRatingDesc() throws Exception {
        String sortingOrder = "desc";
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/movies/genre/{genreId}?rating={sortingOrder}", 1, sortingOrder)
                        .header("sortingOrder", sortingOrder)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getResponseAsString("response/movies/get-movies-by-genre-id-and-rating-desc.json")));
    }

    @Test
    @DataSet(value = "all_dataset.yml", cleanBefore = true, skipCleaningFor = "flyway_schema_history")
    @DisplayName("Test Get Movies By Genre Id And Rating Ascending")
    void testGetMoviesByGenreIdAndRatingAsc() throws Exception {
        String sortingOrder = "asc";
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/movies/genre/{genreId}?rating={sortingOrder}", 1, sortingOrder)
                        .header("sortingOrder", sortingOrder)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getResponseAsString("response/movies/get-movies-by-genre-id-and-rating-asc.json")));
    }

    @Test
    @DataSet(value = "all_dataset.yml", cleanBefore = true, skipCleaningFor = "flyway_schema_history")
    @DisplayName("Test Get Movie By Id")
    void testGetMovieById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/movies/{movieId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(content().json(getResponseAsString("response/movies/get-movie-by-id.json")));
    }

    @Test
    @DataSet(value = "all_dataset.yml", cleanBefore = true, skipCleaningFor = "flyway_schema_history")
    @DisplayName("Test Get Movie By Id If Movie Not Found")
    void testGetMovieByIdIfMovieNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/movies/{movieId}", 100)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}