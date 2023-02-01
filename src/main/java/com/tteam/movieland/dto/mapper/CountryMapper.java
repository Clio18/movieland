package com.tteam.movieland.dto.mapper;

import com.tteam.movieland.dto.CountryDto;
import com.tteam.movieland.entity.Country;
import org.mapstruct.Mapper;

@Mapper
public interface CountryMapper {
    CountryDto toCountryDto (Country country);
}
