package com.tteam.movieland.service.enrichment;

import com.tteam.movieland.dto.MovieDto;
import com.tteam.movieland.dto.mapper.MovieMapper;
import com.tteam.movieland.entity.Country;
import com.tteam.movieland.entity.Genre;
import com.tteam.movieland.entity.Movie;
import com.tteam.movieland.service.CountryService;
import com.tteam.movieland.service.GenreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
@Profile("default")
public class DefaultEnrichMovieService implements EnrichMovieService {
    private final CountryService countryService;
    private final GenreService genreService;
    private final MovieMapper mapper;

    @Override
    public Movie enrich(MovieDto movieDto) {
        log.info("Default enrichment has been started...");
        Set<Long> countriesIds = movieDto.getCountriesId();
        Set<Long> genresIds = movieDto.getGenresId();

        Set<Country> countries;
        if (countriesIds == null) {
            Long id = movieDto.getId();
            countries = countryService.findAllByMovieId(id);
        } else {
            countries = countryService.findAllById(countriesIds);
        }

        Set<Genre> genres;
        if (genresIds == null) {
            Long id = movieDto.getId();
            genres = genreService.findAllByMovieId(id);
        } else {
            genres = genreService.findAllById(genresIds);
        }

        Movie movie = mapper.toMovie(movieDto);
        movie.setCountries(countries);
        movie.setGenres(genres);
        return movie;
    }

}
