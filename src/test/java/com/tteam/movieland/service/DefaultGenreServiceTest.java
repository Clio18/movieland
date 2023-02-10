package com.tteam.movieland.service;

import com.tteam.movieland.entity.Genre;
import com.tteam.movieland.repository.GenreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultGenreServiceTest {

    @Mock
    private GenreRepository genreRepository;
    private GenreService genreService;
    private List<Genre> genres;
    private Genre drama;
    private Genre comedy;

    @BeforeEach
    void init() {
        genreService = new DefaultGenreService(genreRepository);

        drama = Genre.builder()
                .genreName("drama")
                .build();
        comedy = Genre.builder()
                .genreName("comedy")
                .build();
        genres = List.of(drama, comedy);
    }

    @Test
    @DisplayName("test getAll genres and check result is not null, size, content equality, calling the repo's method")
    void testGetAllGenresAndCheckResultNotNullSizeContentCallingTheRepoMethod() {
        when(genreRepository.findAll()).thenReturn(genres);
        List<Genre> actualGenres = genreService.getAll();
        assertNotNull(actualGenres);
        assertEquals(2, actualGenres.size());
        assertEquals(drama, actualGenres.get(0));
        assertEquals(comedy, actualGenres.get(1));
        verify(genreRepository).findAll();
    }

    @Test
    @DisplayName("test getAll genres and check cache usage")
    void testGetAllGenresAndCheckCacheUsage() {
        when(genreRepository.findAll()).thenReturn(genres);
        genreService.getAll();
        verify(genreRepository, times(1)).findAll();
    }
}