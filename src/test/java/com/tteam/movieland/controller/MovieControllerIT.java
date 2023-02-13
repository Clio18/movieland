package com.tteam.movieland.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import com.tteam.movieland.AbstractBaseITest;
import com.tteam.movieland.config.QueryCountTestConfig;
import com.tteam.movieland.dto.MovieDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Set;

import static com.vladmihalcea.sql.SQLStatementCountValidator.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DBRider
@AutoConfigureMockMvc
@Import({QueryCountTestConfig.class})
class MovieControllerIT extends AbstractBaseITest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Test
    @WithMockUser
    @DataSet(value = "all_dataset.yml", cleanBefore = true, skipCleaningFor = "flyway_schema_history")
    @DisplayName("Test GetAll Without Parameters")
    void testGetAllWithoutParameters() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/movies")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getResponseAsString("response/movies/get-all-without-parameters.json")));
        assertSelectCount(2);
    }

    @Test
    @WithMockUser
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
        assertSelectCount(2);
    }

    @Test
    @WithMockUser
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
        assertSelectCount(2);
    }

    @Test
    @WithMockUser
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
        assertSelectCount(3);
    }

    @Test
    @WithMockUser
    @DataSet(value = "all_dataset.yml", cleanBefore = true, skipCleaningFor = "flyway_schema_history")
    @DisplayName("Test Get Movies By Genre Id Without Parameters")
    void testGetMoviesByGenreIdWithoutParameters() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/movies/genre/{genreId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getResponseAsString("response/movies/get-movies-by-genre-id-without-parameters.json")));
        assertSelectCount(2);
    }

    @Test
    @WithMockUser
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
        assertSelectCount(2);
    }

    @Test
    @WithMockUser
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
        assertSelectCount(2);
    }

    @Test
    @WithMockUser
    @DataSet(value = "all_dataset.yml", cleanBefore = true, skipCleaningFor = "flyway_schema_history")
    @DisplayName("Test Get Movie By Id")
    void testGetMovieById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/movies/{movieId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(content().json(getResponseAsString("response/movies/get-movie-by-id.json")));
        assertSelectCount(1);
    }

    @Test
    @WithMockUser
    @DataSet(value = "all_dataset.yml", cleanBefore = true, skipCleaningFor = "flyway_schema_history")
    @DisplayName("Test Get Movie By Id If Movie Not Found")
    void testGetMovieByIdIfMovieNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/movies/{movieId}", 100)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        assertSelectCount(1);
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    @DataSet(value = "response/movies/movies_before_add.yml", cleanBefore = true, skipCleaningFor = "flyway_schema_history")
    @ExpectedDataSet(value = "response/movies/movies_after_add.yml", ignoreCols = "id")
    @DisplayName("Test add movie")
    void whenAdd_thenCorrectJsonReturned() throws Exception {
        MovieDto movieDto = MovieDto.builder()
                .nameUkr("Вищий пілотаж")
                .nameNative("Devotion")
                .yearOfRelease(2022)
                .description("Через початок корейської війни американські пілоти змушені знову підняти в небо своїх вірних бойових птахів")
                .price(150.87)
                .poster("https://uakino.club/uploads/mini/poster/b2/c7e758be3159ff0a1d05a1205f91c7.jpg")
                .countriesId(Set.of(1L))
                .genresId(Set.of(1L))
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/movies/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movieDto))
                        .characterEncoding("utf-8")
                )
                .andExpect(status().isOk());
        assertSelectCount(1);
        assertInsertCount(3);
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    @DataSet(value = "response/movies/movies_before_add.yml", cleanBefore = true, skipCleaningFor = "flyway_schema_history")
    @ExpectedDataSet(value = "response/movies/movies_after_update.yml", ignoreCols = "id")
    @DisplayName("Test update movie")
    void whenUpdate_thenCorrectJsonReturned() throws Exception {
        MovieDto movieDto = MovieDto.builder()
                .nameUkr("Вищий пілотаж")
                .nameNative("Devotion")
                .poster("https://uakino.club/uploads/mini/poster/b2/c7e758be3159ff0a1d05a1205f91c7.jpg")
                .countriesId(Set.of(1L))
                .genresId(Set.of(1L))
                .build();

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/movies/{movieId}", 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movieDto))
                        .characterEncoding("utf-8")
                )
                .andExpect(status().isOk());
        assertSelectCount(3);
        assertDeleteCount(2);
        assertUpdateCount(1);
        assertInsertCount(2);
    }

}