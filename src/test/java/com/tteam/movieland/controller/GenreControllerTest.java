package com.tteam.movieland.controller;

import com.tteam.movieland.entity.Genre;
import com.tteam.movieland.service.GenreService;
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

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class GenreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GenreService genreService;

    private Genre drama;
    private Genre comedy;

    @BeforeEach
    void init() {
        drama = Genre.builder()
                .genreName("drama")
                .build();
        comedy = Genre.builder()
                .genreName("comedy")
                .build();
    }

    @Test
    @DisplayName("Test GetAllGenres And Check Status Code, Result Size, Fields, Service Method Calling")
    void testGetAllGenresAndCheckStatusSizeFieldsServiceMethodCalling() throws Exception {
        List<Genre> genres = List.of(drama, comedy);
        when(genreService.getAll()).thenReturn(genres);
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/api/v1/genres")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].genreName").value("drama"))
                .andExpect(jsonPath("$[1].genreName").value("comedy"));
        verify(genreService).getAll();
    }
}