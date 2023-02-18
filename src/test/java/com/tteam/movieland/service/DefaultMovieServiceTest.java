package com.tteam.movieland.service;

import com.tteam.movieland.entity.Country;
import com.tteam.movieland.entity.Genre;
import com.tteam.movieland.entity.Movie;
import com.tteam.movieland.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultMovieServiceTest {

    @Mock
    private MovieRepository movieRepository;
    @Mock
    private Page<Movie> page;
    @Mock
    private Stream<Movie> stream;

    private MovieService movieService;
    private List<Movie> movies;
    private Movie movie1;
    private Movie movie2;

    @BeforeEach
    void init() {
        movieService = new DefaultMovieService(movieRepository);

        Country usa = Country.builder()
                .name("usa")
                .build();
        Country australia = Country.builder()
                .name("australia")
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
        movies = List.of(movie1, movie2);
    }


    @Test
    @DisplayName("test getMoviesByGenreId and check result is not null, size, content equality, calling the repo's method")
    void testGetMoviesByGenreId_AndCheckResultNotNull_Size_Content_CallingTheRepoMethod() {
        when(movieRepository.findByGenresId(PageRequest.of(0, 3), 1L)).thenReturn(page);
        when(page.stream()).thenReturn(stream);
        when(stream.toList()).thenReturn(movies);
        List<Movie> actualMovies = movieService.getMoviesByGenreId(1L);
        assertNotNull(actualMovies);
        assertEquals(2, actualMovies.size());
        assertEquals(movie1, actualMovies.get(0));
        assertEquals(movie2, actualMovies.get(1));
        verify(movieRepository).findByGenresId(PageRequest.of(0, 3), 1L);
    }

    @Test
    @DisplayName("test getById and check result is not null")
    void testGetById_AndCheckResultNotNull() {
        when(movieRepository.findById(1L)).thenReturn(Optional.ofNullable(movie1));
        Movie actualMovie = movieService.getById(1L);
        assertNotNull(actualMovie);
        assertEquals(movie1, actualMovie);
        verify(movieRepository).findById(1L);
    }

}