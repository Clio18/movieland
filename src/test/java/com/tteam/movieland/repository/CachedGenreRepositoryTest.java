package com.tteam.movieland.repository;

import com.tteam.movieland.entity.Genre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CachedGenreRepositoryTest {
    private CachedGenreRepository genreRepository;
    private List<Genre> expectedGenres;
    @Mock
    private JpaGenreRepository jpaGenreRepository;

    @BeforeEach
    void setUp() {
        genreRepository = new CachedGenreRepository(jpaGenreRepository);
        expectedGenres = List.of(new Genre(1L, "Action"), new Genre(2L, "Drama"), new Genre(3L, "Comedy"));
    }

    @Test
    @DisplayName("Test Genre repository findAll method")
    void testFindAllReturnsExpectedNumberOfGenres() {
        when(jpaGenreRepository.findAll()).thenReturn(expectedGenres);

        List<Genre> actualGenres = genreRepository.findAll();

        assertEquals(expectedGenres.size(), actualGenres.size());
        verify(jpaGenreRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Test Genre repository findAll method updates if cache is empty")
    public void testFindAllUpdatesIfCacheEmpty() {
        genreRepository.cachedGenreList = null;
        genreRepository.updateGenresCache();
        when(jpaGenreRepository.findAll()).thenReturn(expectedGenres);

        List<Genre> actualGenres = genreRepository.findAll();

        assertEquals(expectedGenres.size(), actualGenres.size());
        verify(jpaGenreRepository, times(2)).findAll();
    }

    @Test
    @DisplayName("Test Genre repository findAllById method")
    void testFindAllById() {
        Set<Long> genreIds = new HashSet<>(Arrays.asList(1L, 2L, 3L));
        when(jpaGenreRepository.findAllById(genreIds)).thenReturn(expectedGenres);

        Set<Genre> actualGenres = genreRepository.findAllById(genreIds);

        assertEquals(new HashSet<>(expectedGenres), actualGenres);
        verify(jpaGenreRepository, times(1)).findAllById(genreIds);
    }


    @Test
    @DisplayName("Test Genre repository findAllByMovieId method")
    void testFindAllByMovieId() {
        when(jpaGenreRepository.findAllByMovieId(1L)).thenReturn(new HashSet<>(expectedGenres));

        Set<Genre> actualGenres = genreRepository.findAllByMovieId(1L);

        assertEquals(new HashSet<>(expectedGenres), actualGenres);
        verify(jpaGenreRepository, times(1)).findAllByMovieId(1L);
    }

}