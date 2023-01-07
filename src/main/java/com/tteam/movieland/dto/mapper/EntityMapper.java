package com.tteam.movieland.dto.mapper;

import com.tteam.movieland.dto.ResponseMovie;
import com.tteam.movieland.entity.Movie;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author Oleksandr Shevchenko
 */
@Mapper
public interface EntityMapper {
    EntityMapper INSTANCE = Mappers.getMapper(EntityMapper.class);
    ResponseMovie movieToResponseMovie(Movie movie);
}
