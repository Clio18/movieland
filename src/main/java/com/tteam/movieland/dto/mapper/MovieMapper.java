package com.tteam.movieland.dto.mapper;

import com.tteam.movieland.dto.*;
import com.tteam.movieland.entity.Country;
import com.tteam.movieland.entity.Genre;
import com.tteam.movieland.entity.Movie;
import org.mapstruct.*;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper
public interface MovieMapper {

    @Mapping(source = "countries", target = "countriesId", qualifiedByName = "mapCountriesToCountriesId")
    @Mapping(source = "genres", target = "genresId", qualifiedByName = "mapGenresToGenresId")
    MovieDto toMovieDto(Movie movie);
    Movie toMovie(MovieDto movieDto);

    //under question - do we need show only countries ID when show movie?
    @Named("mapCountriesToCountriesId")
    static Set<Long> mapCountriesToCountriesId(Set<Country> countries) {
        return countries.stream().map(Country::getId)
                .collect(Collectors.toSet());
    }

    //under question - do we need show only genres ID when show movie?
    @Named("mapGenresToGenresId")
    static Set<Long> mapGenresToGenresId(Set<Genre> genres) {
        return genres.stream().map(Genre::getId)
                .collect(Collectors.toSet());
    }
}
