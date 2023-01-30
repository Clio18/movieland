package com.tteam.movieland.service;

import com.tteam.movieland.dto.CountryDto;
import com.tteam.movieland.dto.GenreDto;
import com.tteam.movieland.dto.MovieDto;
import com.tteam.movieland.dto.MovieWithCountriesAndGenresDto;
import com.tteam.movieland.dto.mapper.MovieMapper;
import com.tteam.movieland.entity.Country;
import com.tteam.movieland.entity.Genre;
import com.tteam.movieland.entity.Movie;
import com.tteam.movieland.exception.MovieNotFoundException;
import com.tteam.movieland.repository.CountryRepository;
import com.tteam.movieland.repository.JpaGenreRepository;
import com.tteam.movieland.repository.MovieRepository;
import com.tteam.movieland.service.impl.MovieServiceDefault;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieServiceDefaultTest {

    @Mock
    private MovieRepository movieRepository;
    @Mock
    private CountryRepository countryRepository;
    @Mock
    private JpaGenreRepository genreRepository;
    @Mock
    private MovieMapper mapper;
    @Mock
    private Page<Movie> page;
    @Mock
    private Stream<Movie> stream;

    private MovieService movieService;
    private List<Movie> movies;
    private Movie movie1;
    private Movie movie2;

    private MovieDto movieDto;
    private Set<Country> countries;
    private Set<Genre> genres;

    private MovieWithCountriesAndGenresDto movieWithCountriesAndGenresDto;

    @BeforeEach
    void init() {
        movieService = new MovieServiceDefault(movieRepository, countryRepository, genreRepository, mapper);

        Country usa = Country.builder()
                .countryName("usa")
                .build();
        Country australia = Country.builder()
                .countryName("australia")
                .build();
        countries = Set.of(usa, australia);

        Genre drama = Genre.builder()
                .genreName("drama")
                .build();
        Genre comedy = Genre.builder()
                .genreName("comedy")
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
                .genresId(Set.of(1L, 2L, 3L))
                .build();
        movieWithCountriesAndGenresDto = MovieWithCountriesAndGenresDto.builder()
                .nameUkr("Matrix")
                .nameNative("Matrix")
                .yearOfRelease(1989)
                .description("Best movie")
                .price(10.0)
                .rating(10.0)
                .poster("url/")
                .countriesDto(Set.of(
                        CountryDto.builder().countryName("usa").build(),
                        CountryDto.builder().countryName("ukraine").build()
                ))
                .genresDto(Set.of(
                        GenreDto.builder().genreName("comedy").build(),
                        GenreDto.builder().genreName("love").build(),
                        GenreDto.builder().genreName("drama").build()
                ))
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
        assertEquals(movie1, actualMovie);
        verify(movieRepository).findById(1L);
    }

    @Test
    @DisplayName("Test save and check result is not null, contains lists of countries and genres, verify methods call")
    void testSave_AndCheckNotNullResultAndMethodCall() {
        when(mapper.toMovie(movieDto)).thenReturn(movie1);
        when(countryRepository.findAllById(movieDto.getCountriesId())).thenReturn(new ArrayList<>(countries));
        when(genreRepository.findAllById(movieDto.getGenresId())).thenReturn(new ArrayList<>(genres));
        when(mapper.toWithCountriesAndGenresDto(movie1)).thenReturn(movieWithCountriesAndGenresDto);

        MovieWithCountriesAndGenresDto movieWithCountriesAndGenresDto = movieService.saveMovieWithGenresAndCountries(movieDto);
        assertAll(
                () -> assertNotNull(movieWithCountriesAndGenresDto),
                () -> assertEquals(2, movieWithCountriesAndGenresDto.getCountriesDto().size()),
                () -> assertEquals(3, movieWithCountriesAndGenresDto.getGenresDto().size()));

        verify(countryRepository).findAllById(movieDto.getCountriesId());
        verify(genreRepository).findAllById(movieDto.getGenresId());
        verify(movieRepository).save(movie1);
    }

    @Test
    @DisplayName("Test update and check result is not null, contains lists of countries and genres, verify methods call")
    void testUpdate_AndCheckNotNullResultAndMethodCall() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie1));
        when(mapper.update(movie1, movieDto)).thenReturn(movie1);
        when(countryRepository.findAllById(movieDto.getCountriesId())).thenReturn(new ArrayList<>(countries));
        when(genreRepository.findAllById(movieDto.getGenresId())).thenReturn(new ArrayList<>(genres));
        when(mapper.toWithCountriesAndGenresDto(movie1)).thenReturn(movieWithCountriesAndGenresDto);

        MovieWithCountriesAndGenresDto movieWithCountriesAndGenresDto = movieService.updateMovieWithGenresAndCountries(1L, movieDto);
        assertAll(
                () -> assertNotNull(movieWithCountriesAndGenresDto),
                () -> assertEquals(2, movieWithCountriesAndGenresDto.getCountriesDto().size()),
                () -> assertEquals(3, movieWithCountriesAndGenresDto.getGenresDto().size()));

        verify(countryRepository).findAllById(movieDto.getCountriesId());
        verify(genreRepository).findAllById(movieDto.getGenresId());
        verify(movieRepository).save(movie1);
    }

    @Test
    @DisplayName("Test update and check exception thrown")
    void testUpdate_AndCheckExceptionThrown() {
        when(movieRepository.findById(1L)).thenThrow(MovieNotFoundException.class);
        assertThrows(MovieNotFoundException.class, () -> {
            movieService.updateMovieWithGenresAndCountries(1L, movieDto);
        });
    }

}