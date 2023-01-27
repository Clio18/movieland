package com.tteam.movieland.dto.mapper;

import com.tteam.movieland.dto.GenreDto;
import com.tteam.movieland.dto.MovieDto;
import com.tteam.movieland.entity.Genre;
import com.tteam.movieland.entity.Movie;
import org.mapstruct.Mapper;

@Mapper
public interface EntityMapper {

    MovieDto toMovieDto(Movie movie);
    GenreDto toGenreDto(Genre movie);
}
