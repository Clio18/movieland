package com.tteam.movieland.controller;

import com.tteam.movieland.dto.GenreDto;
import com.tteam.movieland.dto.mapper.EntityMapper;
import com.tteam.movieland.entity.Genre;
import com.tteam.movieland.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;
    @Autowired
    private EntityMapper mapper;

    @GetMapping("genres")
    protected List<GenreDto> getAllGenres() {
        List<Genre> genres = genreService.getAll();
        return genres.stream().map(mapper::entityToDto).toList();
    }
}
