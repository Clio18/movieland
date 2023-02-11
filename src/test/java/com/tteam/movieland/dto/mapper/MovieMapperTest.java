package com.tteam.movieland.dto.mapper;

import com.tteam.movieland.dto.MovieDto;
import com.tteam.movieland.entity.Country;
import com.tteam.movieland.entity.Genre;
import com.tteam.movieland.entity.Movie;
import com.tteam.movieland.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MovieMapperTest {
    private final MovieMapper mapper = new MovieMapper() {
        @Override
        public MovieDto toMovieDto(Movie movie) {
            if ( movie == null ) {
                return null;
            }

            MovieDto.MovieDtoBuilder movieDto = MovieDto.builder();

            movieDto.id( movie.getId() );
            movieDto.nameUkr( movie.getNameUkr() );
            movieDto.nameNative( movie.getNameNative() );
            movieDto.yearOfRelease( movie.getYearOfRelease() );
            movieDto.description( movie.getDescription() );
            movieDto.price( movie.getPrice() );
            movieDto.rating( movie.getRating() );
            movieDto.poster( movie.getPoster() );

            return movieDto.build();
        }

        @Override
        public Movie toMovie(MovieDto movieDto) {
            if ( movieDto == null ) {
                return null;
            }

            Movie.MovieBuilder movie = Movie.builder();

            movie.id( movieDto.getId() );
            movie.nameUkr( movieDto.getNameUkr() );
            movie.nameNative( movieDto.getNameNative() );
            movie.yearOfRelease( movieDto.getYearOfRelease() );
            movie.description( movieDto.getDescription() );
            movie.price( movieDto.getPrice() );
            movie.rating( movieDto.getRating() );
            movie.poster( movieDto.getPoster() );

            return movie.build();
        }
    };
    @MockBean
    private MovieService movieService;
    private Movie movie;
    private MovieDto movieDto;

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
                .description("Best movie")
                .build();
    }

    @Test
    void testToMovieDtoShouldConvertCorrectly() {
        MovieDto movieDto = mapper.toMovieDto(movie);
        assertEquals(this.movieDto, movieDto);
    }
}