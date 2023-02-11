package com.tteam.movieland.dto.mapper;

import com.tteam.movieland.dto.MovieDto;
import com.tteam.movieland.entity.Country;
import com.tteam.movieland.entity.Genre;
import com.tteam.movieland.entity.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {MovieMapperImpl.class})
class MovieMapperTest {
    @Autowired
    private MovieMapper mapper;
    private Movie movie;
    private MovieDto movieDto;

    @BeforeEach
    void init(){
        Country usa = Country.builder()
                .id(1L)
                .countryName("usa")
                .build();
        Country australia = Country.builder()
                .id(2L)
                .countryName("australia")
                .build();
        Set<Country> countries = Set.of(usa, australia);

        Genre drama = Genre.builder()
                .id(1L)
                .genreName("drama")
                .build();
        Genre comedy = Genre.builder()
                .id(2L)
                .genreName("comedy")
                .build();
        Set<Genre> genres = Set.of(drama, comedy);

        movie = Movie.builder()
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

        movieDto = MovieDto.builder()
                .price(10.0)
                .nameUkr("Matrix")
                .nameNative("Matrix")
                .yearOfRelease(1989)
                .rating(10.0)
                .poster("url/")
                .countriesId(Set.of(1L,2L))
                .genresId(Set.of(1L,2L))
                .description("Best movie")
                .build();
    }

    @Test
    void testToMovieDtoShouldConvertCorrectly() {
        MovieDto movieDto = mapper.toMovieDto(movie);
        assertEquals(this.movieDto, movieDto);
    }
}