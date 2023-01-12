package com.tteam.movieland.dto.mapper;

import com.tteam.movieland.dto.GenreDto;
import com.tteam.movieland.dto.MovieDto;
import com.tteam.movieland.dto.UserDto;
import com.tteam.movieland.entity.Genre;
import com.tteam.movieland.entity.Movie;
import com.tteam.movieland.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface EntityMapper {
    MovieDto entityToDto(Movie movie);
    GenreDto entityToDto(Genre genre);

    UserDto entityToDto(User user);

    @Mapping(target = "id", ignore = true)
    User dtoToEntity(UserDto userDto);
}
