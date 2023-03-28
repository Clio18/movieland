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
    MovieDto toMovieDto(Movie movie);

    MovieWithReviewDto toWithReviewDto(Movie movie);

    Movie toMovie(MovieDto movieDto);

    @Mapping(source = "countries", target = "countriesDto", qualifiedByName = "mapToSetCountryDto")
    @Mapping(source = "genres", target = "genresDto", qualifiedByName = "mapToSetGenreDto")
    MovieWithCountriesAndGenresDto toWithCountriesAndGenresDto(Movie movie);

    // =================== country =====================
    @Named("mapToSetCountryDto")
    @IterableMapping(qualifiedByName = "mapCountryDtoWithoutData")
    static Set<CountryDto> mapToSetCountryDto(Set<Country> value) {
        return value.stream().map(c -> CountryDto.builder()
                        .id(c.getId())
                        .name(c.getName())
                        .build())
                .collect(Collectors.toSet());
    }

    @Named("mapCountryDtoWithoutData")
    @Mapping(source = "id", target = "id", ignore = true)
    CountryDto mapCountryDtoWithoutData(Country source);

    // =================== genre =====================
    @Named("mapToSetGenreDto")
    @IterableMapping(qualifiedByName = "mapGenreDtoWithoutData")
    static Set<GenreDto> mapToSetGenreDto(Set<Genre> value) {
        return value.stream().map(g -> GenreDto.builder()
                        .id(g.getId())
                        .name(g.getName())
                        .build())
                .collect(Collectors.toSet());
    }

    @Named("mapGenreDtoWithoutData")
    @Mapping(source = "id", target = "id", ignore = true)
    GenreDto mapGenreDtoWithoutData(Genre source);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "genres", ignore = true)
    @Mapping(target = "countries", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    Movie update(@MappingTarget Movie movie, MovieDto movieDto);
}
