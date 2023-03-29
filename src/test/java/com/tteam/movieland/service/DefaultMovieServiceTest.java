package com.tteam.movieland.service;

import com.tteam.movieland.dto.MovieDto;
import com.tteam.movieland.entity.Country;
import com.tteam.movieland.entity.Genre;
import com.tteam.movieland.entity.Movie;
import com.tteam.movieland.repository.MovieRepository;
import com.tteam.movieland.service.enrichment.EnrichMovieService;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultMovieServiceTest {

    @Mock
    private MovieRepository movieRepository;
    @Mock
    private CurrencyService currencyService;
    @Mock
    private Page<Movie> page;
    @Mock
    private Stream<Movie> stream;
    @Mock
    private EnrichMovieService enrichMovieService;

    private MovieService movieService;
    private List<Movie> movies;
    private Movie movie1;
    private Movie movie2;

    private MovieDto movieDto;
    private Set<Country> countries;
    private Set<Genre> genres;

    @BeforeEach
    void init() {
        movieService = new DefaultMovieService(enrichMovieService, movieRepository, currencyService);

        Country usa = Country.builder()
                .name("usa")
                .build();
        Country australia = Country.builder()
                .name("australia")
                .build();
        countries = Set.of(usa, australia);

        Genre drama = Genre.builder()
                .name("drama")
                .build();
        Genre comedy = Genre.builder()
                .name("comedy")
                .build();
        genres = Set.of(drama, comedy);

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
        movieDto = MovieDto.builder()
                .nameUkr("Matrix")
                .nameNative("Matrix")
                .yearOfRelease(1989)
                .description("Best movie")
                .price(10.0)
                .rating(10.0)
                .poster("url/")
                .countriesId(Set.of(1L, 2L))
                .genresId(Set.of(1L, 2L))
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
    @DisplayName("Test getById and check result is not null")
    void testGetById_AndCheckResultNotNull() {
        String currency = "UAH";
        when(movieRepository.findById(1L)).thenReturn(Optional.ofNullable(movie1));
        Movie actualMovie = movieService.getById(1L, currency);
        assertNotNull(actualMovie);
        assertEquals(movie1.getNameUkr(), actualMovie.getNameUkr());
        verify(movieRepository).findById(1L);
    }

    @Test
    @DisplayName("Test save and check result is not null, contains lists of countries and genres, verify methods call")
    void testSave_AndCheckNotNullResultAndMethodCall() {
        when(enrichMovieService.enrich(movieDto)).thenReturn(movie1);
        Movie movie = movieService.saveMovieWithGenresAndCountries(movieDto);
        assertAll(
                () -> assertNotNull(movie),
                () -> assertEquals(2, movie.getCountries().size()),
                () -> assertEquals(2, movie.getGenres().size()));

        verify(movieRepository).save(movie1);
    }

    @Test
    @DisplayName("Test update and check result is not null, contains lists of countries and genres, verify methods call")
    void testUpdate_AndCheckNotNullResultAndMethodCall() {
        when(enrichMovieService.enrich(movieDto)).thenReturn(movie1);
        Movie movie = movieService.updateMovieWithGenresAndCountries(1L, movieDto);
        assertAll(
                () -> assertNotNull(movie),
                () -> assertEquals(2, movie.getCountries().size()),
                () -> assertEquals(2, movie.getGenres().size()));

        verify(movieRepository).save(movie1);
    }

    @Test
    @DisplayName("Test Cached GetById Calling And Check No Interactions With DB")
    void testCachedGetById_CheckNoInteractionsWithDB(){
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie1));
        movieService.getById(1L, "UAH");
        movieService.getById(1L, "UAH");
        verify(movieRepository, times(1)).findById(1L);
    }
}
