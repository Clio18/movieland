package com.tteam.movieland.dto.mapper;

import com.tteam.movieland.dto.GenreDto;
import com.tteam.movieland.entity.Genre;
import org.mapstruct.Mapper;

@Mapper
public interface GenreMapper {
    GenreDto toGenreDto(Genre genre);
}

