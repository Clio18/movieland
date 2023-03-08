package com.tteam.movieland.controller;

import com.tteam.movieland.AbstractBaseITest;
import com.tteam.movieland.dto.GenreDto;
import com.tteam.movieland.dto.mapper.GenreMapper;
import com.tteam.movieland.entity.Genre;
import com.tteam.movieland.service.DefaultGenreService;
import com.tteam.movieland.service.GenreService;
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

import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class GenreControllerTest extends AbstractBaseITest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GenreMapper mapper;

    @MockBean
    private DefaultGenreService genreService;

    private Genre drama;
    private Genre comedy;

    private GenreDto dramaDto;
    private GenreDto comedyDto;

    @BeforeEach
    void init() {
        drama = Genre.builder()
                .name("drama")
                .build();
        comedy = Genre.builder()
                .name("comedy")
                .build();
        dramaDto = GenreDto.builder()
                .name("drama")
                .build();
        comedyDto = GenreDto.builder()
                .name("comedy")
                .build();
    }

    @Test
    @WithMockUser
    @DisplayName("Test GetAllGenres And Check Status Code, Result Size, Fields, Service Method Calling")
    void testGetAllGenresAndCheckStatusSizeFieldsServiceMethodCalling() throws Exception {
        List<Genre> genres = List.of(drama, comedy);
        when(genreService.getAll()).thenReturn(genres);
        when(mapper.toGenreDto(drama)).thenReturn(dramaDto);
        when(mapper.toGenreDto(comedy)).thenReturn(comedyDto);
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/api/v1/genres")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("drama"))
                .andExpect(jsonPath("$[1].name").value("comedy"));
        verify(genreService).getAll();
    }
}