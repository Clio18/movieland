package com.tteam.movieland.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tteam.movieland.entity.Country;
import com.tteam.movieland.entity.Genre;
import com.tteam.movieland.entity.Movie;
import com.tteam.movieland.exception.CurrencyNotFoundException;
import com.tteam.movieland.exception.GenreNotFoundException;
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
    private Movie movie3;

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
                .genreName("drama")
                .build();
        Genre comedy = Genre.builder()
                .genreName("comedy")
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
        movie3 = Movie.builder()
                .price(0.27)
                .nameUkr("Matrix")
                .nameNative("Matrix")
                .yearOfRelease(1989)
                .rating(10.0)
                .poster("url/")
                .description("Best movie")
                .countries(countries)
                .genres(genres)
                .build();
    }

    @Test
    @DisplayName("""
    Test GetAll Rating Descending Order And Check Status Code, Result Size, Fields,
    Service Method Calling""")
    void testGetAllDescRating_AndCheckStatus_Size_Fields_ServiceMethodCalling() throws Exception {
        List<Movie> movies = List.of(movie1, movie2);
        String sortingOrder = "desc";
        when(movieService.getAllSortedByRating(sortingOrder)).thenReturn(movies);
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/api/v1/movies?rating={sortingOrder}", sortingOrder)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].price").value(10.0))
                .andExpect(jsonPath("$[0].nameUkr").value("Matrix"))
                .andExpect(jsonPath("$[0].nameNative").value("Matrix"))
                .andExpect(jsonPath("$[0].yearOfRelease").value(1989))
                .andExpect(jsonPath("$[0].rating").value(10.0))
                .andExpect(jsonPath("$[0].poster").value("url/"))
                .andExpect(jsonPath("$[0].description").value("Best movie"))

                .andExpect(jsonPath("$[1].price").value(12.0))
                .andExpect(jsonPath("$[1].nameUkr").value("Matrix2"))
                .andExpect(jsonPath("$[1].nameNative").value("Matrix2"))
                .andExpect(jsonPath("$[1].yearOfRelease").value(1999))
                .andExpect(jsonPath("$[1].rating").value(9.0))
                .andExpect(jsonPath("$[1].poster").value("url/"))
                .andExpect(jsonPath("$[1].description").value("Good movie"));

        verify(movieService).getAllSortedByRating(sortingOrder);
    }

    @Test
    @DisplayName("""
    Test GetAll Rating Descending Order And Check Status Code, Result Size, Fields,
    Service Method Calling""")
    void testGetAllAscRating_AndCheckStatus_Size_Fields_ServiceMethodCalling() throws Exception {
        List<Movie> movies = List.of(movie2, movie1);
        String sortingOrder = "asc";
        when(movieService.getAllSortedByRating(sortingOrder)).thenReturn(movies);
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/api/v1/movies?rating={sortingOrder}", sortingOrder)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].price").value(12.0))
                .andExpect(jsonPath("$[0].nameUkr").value("Matrix2"))
                .andExpect(jsonPath("$[0].nameNative").value("Matrix2"))
                .andExpect(jsonPath("$[0].yearOfRelease").value(1999))
                .andExpect(jsonPath("$[0].rating").value(9.0))
                .andExpect(jsonPath("$[0].poster").value("url/"))
                .andExpect(jsonPath("$[0].description").value("Good movie"))

                .andExpect(jsonPath("$[1].price").value(10.0))
                .andExpect(jsonPath("$[1].nameUkr").value("Matrix"))
                .andExpect(jsonPath("$[1].nameNative").value("Matrix"))
                .andExpect(jsonPath("$[1].yearOfRelease").value(1989))
                .andExpect(jsonPath("$[1].rating").value(10.0))
                .andExpect(jsonPath("$[1].poster").value("url/"))
                .andExpect(jsonPath("$[1].description").value("Best movie"));

        verify(movieService).getAllSortedByRating(sortingOrder);
    }

    @Test
    @DisplayName("""
    Test GetAllMovies No Rating Ordering And Check Status Code, Result Size, Fields, Service Method Calling""")
    void testGetAllMoviesNoRatingOrdering_AndCheckStatus_Size_Fields_ServiceMethodCalling() throws Exception {
        List<Movie> movies = List.of(movie1, movie2);
        String sortingOrder = "";
        when(movieService.getAllSortedByRating(sortingOrder)).thenReturn(movies);
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/api/v1/movies?rating={sortingOrder}", sortingOrder)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].price").value(10.0))
                .andExpect(jsonPath("$[0].nameUkr").value("Matrix"))
                .andExpect(jsonPath("$[0].nameNative").value("Matrix"))
                .andExpect(jsonPath("$[0].yearOfRelease").value(1989))
                .andExpect(jsonPath("$[0].rating").value(10.0))
                .andExpect(jsonPath("$[0].poster").value("url/"))
                .andExpect(jsonPath("$[0].description").value("Best movie"))

                .andExpect(jsonPath("$[1].price").value(12.0))
                .andExpect(jsonPath("$[1].nameUkr").value("Matrix2"))
                .andExpect(jsonPath("$[1].nameNative").value("Matrix2"))
                .andExpect(jsonPath("$[1].yearOfRelease").value(1999))
                .andExpect(jsonPath("$[1].rating").value(9.0))
                .andExpect(jsonPath("$[1].poster").value("url/"))
                .andExpect(jsonPath("$[1].description").value("Good movie"));

        verify(movieService).getAllSortedByRating(sortingOrder);
    }

    @Test
    @DisplayName("Test GetRandomMovie And Check Status Code, Result Size, Fields, Service Method Calling")
    void testGetRandomMovie_AndCheckStatus_Size_Fields_ServiceMethodCalling() throws Exception {
        List<Movie> movies = List.of(movie1, movie2);
        when(movieService.getThreeRandom()).thenReturn(movies);
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/api/v1/movies/random")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].rating").value(10.0))
                .andExpect(jsonPath("$[1].rating").value(9.0));
        verify(movieService).getThreeRandom();
    }

    @Test
    @DisplayName("""
    Test GetMoviesByGenreId And Rating Ascending Order And Check Status Code,
    Result Size, Fields, Service Method Calling""")
    void testGetMoviesByGenreIdAndRatingAsc_AndCheckStatus_Size_Fields_ServiceMethodCalling() throws Exception {
        List<Movie> movies = List.of(movie1, movie2);
        String sortingOrder = "asc";
        when(movieService.getMoviesByGenreSortedByRating(1L, sortingOrder)).thenReturn(movies);
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/api/v1/movies/genre/{genreId}?rating={sortingOrder}", 1L, sortingOrder)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].price").value(10.0))
                .andExpect(jsonPath("$[0].nameUkr").value("Matrix"))
                .andExpect(jsonPath("$[0].nameNative").value("Matrix"))
                .andExpect(jsonPath("$[0].yearOfRelease").value(1989))
                .andExpect(jsonPath("$[0].rating").value(10.0))
                .andExpect(jsonPath("$[0].poster").value("url/"))
                .andExpect(jsonPath("$[0].description").value("Best movie"))

                .andExpect(jsonPath("$[1].price").value(12.0))
                .andExpect(jsonPath("$[1].nameUkr").value("Matrix2"))
                .andExpect(jsonPath("$[1].nameNative").value("Matrix2"))
                .andExpect(jsonPath("$[1].yearOfRelease").value(1999))
                .andExpect(jsonPath("$[1].rating").value(9.0))
                .andExpect(jsonPath("$[1].poster").value("url/"))
                .andExpect(jsonPath("$[1].description").value("Good movie"));

        verify(movieService).getMoviesByGenreSortedByRating(1L, sortingOrder);
    }

    @Test
    @DisplayName("Test GetMoviesByGenreId And Rating Ascending Order If Genre Not Found")
    void testGetMoviesByGenreIdAndRatingAsc_IfGenreNotFound() throws Exception {
        String sortingOrder = "asc";
        when(movieService.getMoviesByGenreSortedByRating(1L, sortingOrder)).thenThrow(GenreNotFoundException.class);
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/api/v1/movies/genre/{genreId}?rating={sortingOrder}", 1L, sortingOrder)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(movieService).getMoviesByGenreSortedByRating(1L, sortingOrder);
    }

    @Test
    @DisplayName("""
    Test GetMoviesByGenreId And Rating Descending Order And Check Status Code,
    Result Size, Fields, Service Method Calling""")
    void testGetMoviesByGenreIdAndRatingDesc_AndCheckStatus_Size_Fields_ServiceMethodCalling() throws Exception {
        List<Movie> movies = List.of(movie1, movie2);
        String sortingOrder = "desc";
        when(movieService.getMoviesByGenreSortedByRating(1L, sortingOrder)).thenReturn(movies);
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/api/v1/movies/genre/{genreId}?rating={sortingOrder}", 1L, sortingOrder)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].price").value(10.0))
                .andExpect(jsonPath("$[0].nameUkr").value("Matrix"))
                .andExpect(jsonPath("$[0].nameNative").value("Matrix"))
                .andExpect(jsonPath("$[0].yearOfRelease").value(1989))
                .andExpect(jsonPath("$[0].rating").value(10.0))
                .andExpect(jsonPath("$[0].poster").value("url/"))
                .andExpect(jsonPath("$[0].description").value("Best movie"))

                .andExpect(jsonPath("$[1].price").value(12.0))
                .andExpect(jsonPath("$[1].nameUkr").value("Matrix2"))
                .andExpect(jsonPath("$[1].nameNative").value("Matrix2"))
                .andExpect(jsonPath("$[1].yearOfRelease").value(1999))
                .andExpect(jsonPath("$[1].rating").value(9.0))
                .andExpect(jsonPath("$[1].poster").value("url/"))
                .andExpect(jsonPath("$[1].description").value("Good movie"));

        verify(movieService).getMoviesByGenreSortedByRating(1L, sortingOrder);
    }

    @Test
    @DisplayName("Test GetMoviesByGenreId And Rating Descending Order If Genre Not Found")
    void testGetMoviesByGenreIdAndRatingDesc_IfGenreNotFound() throws Exception {
        String sortingOrder = "desc";
        when(movieService.getMoviesByGenreSortedByRating(1L, sortingOrder)).thenThrow(GenreNotFoundException.class);
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/api/v1/movies/genre/{genreId}?rating={sortingOrder}", 1L, sortingOrder)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(movieService).getMoviesByGenreSortedByRating(1L, sortingOrder);
    }

    @Test
    @DisplayName("""
    Test GetMoviesByGenreId And No Rating Ordering And Check Status Code,
    Result Size, Fields, Service Method Calling""")
    void testGetMoviesByGenreIdAndNoRatingOrdering_AndCheckStatus_Size_Fields_ServiceMethodCalling() throws Exception {
        List<Movie> movies = List.of(movie1, movie2);
        String sortingOrder = "";
        when(movieService.getMoviesByGenreSortedByRating(1L, sortingOrder)).thenReturn(movies);
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/api/v1/movies/genre/{genreId}?rating={sortingOrder}", 1L, sortingOrder)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].price").value(10.0))
                .andExpect(jsonPath("$[0].nameUkr").value("Matrix"))
                .andExpect(jsonPath("$[0].nameNative").value("Matrix"))
                .andExpect(jsonPath("$[0].yearOfRelease").value(1989))
                .andExpect(jsonPath("$[0].rating").value(10.0))
                .andExpect(jsonPath("$[0].poster").value("url/"))
                .andExpect(jsonPath("$[0].description").value("Best movie"))

                .andExpect(jsonPath("$[1].price").value(12.0))
                .andExpect(jsonPath("$[1].nameUkr").value("Matrix2"))
                .andExpect(jsonPath("$[1].nameNative").value("Matrix2"))
                .andExpect(jsonPath("$[1].yearOfRelease").value(1999))
                .andExpect(jsonPath("$[1].rating").value(9.0))
                .andExpect(jsonPath("$[1].poster").value("url/"))
                .andExpect(jsonPath("$[1].description").value("Good movie"));

        verify(movieService).getMoviesByGenreSortedByRating(1L, sortingOrder);
    }

    @Test
    @DisplayName("Test GetMoviesByGenreId And No Rating Ordering If Genre Not Found")
    void testGetMoviesByGenreIdAndNoRatingOrdering_IfGenreNotFound() throws Exception {
        String sortingOrder = "";
        when(movieService.getMoviesByGenreSortedByRating(1L, sortingOrder)).thenThrow(GenreNotFoundException.class);
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/api/v1/movies/genre/{genreId}?rating={sortingOrder}", 1L, sortingOrder)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(movieService).getMoviesByGenreSortedByRating(1L, sortingOrder);
    }

    @Test
    @DisplayName("Test GetMovieById And Check Status Code")
    void testGetMovieById_AndCheckStatus() throws Exception {
        String currency = "UAH";
        when(movieService.getById(1L, currency)).thenReturn(movie1);
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/api/v1/movies/{movieId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nameUkr").value("Matrix"))
                .andExpect(jsonPath("$.price").value(10.0));
        verify(movieService).getById(1L, currency);
    }

    @Test
    @DisplayName("Test GetMovieById With Defined Currency And Check Status Code")
    void testGetMovieByIdWithDefinedCurrency_AndCheckStatus() throws Exception {
        String currency = "USD";
        when(movieService.getById(1L, currency)).thenReturn(movie3);
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/api/v1/movies/{movieId}?currency={currency}", 1L, currency)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nameUkr").value("Matrix"))
                .andExpect(jsonPath("$.price").value(0.27));
        verify(movieService).getById(1L, currency);
    }

    @Test
    @DisplayName("Test GetMovieById If Movie Not Found")
    void testGetMovieById_IfMovieNotFound() throws Exception {
        String currency = "UAH";
        when(movieService.getById(1L, currency)).thenThrow(MovieNotFoundException.class);
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/api/v1/movies/{movieId}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(movieService).getById(1L, currency);
    }

    @Test
    @DisplayName("Test GetMovieById If Wrong Currency")
    void testGetMovieById_IfWrongCurrency() throws Exception {
        String currency = "GHTR";
        when(movieService.getById(1L, currency)).thenThrow(CurrencyNotFoundException.class);
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/api/v1/movies/{movieId}?currency={currency}", 1L, currency)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(movieService).getById(1L, currency);
    }
}