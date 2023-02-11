package com.tteam.movieland.dto.mapper;

import com.tteam.movieland.dto.*;
import com.tteam.movieland.entity.Movie;
import org.mapstruct.*;

@Mapper
public interface MovieMapper {
    MovieDto toMovieDto(Movie movie);

    Movie toMovie(MovieDto movieDto);
}
