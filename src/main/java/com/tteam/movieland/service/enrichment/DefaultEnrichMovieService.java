package com.tteam.movieland.service.enrichment;

import com.tteam.movieland.dto.MovieDto;
import com.tteam.movieland.entity.Country;
import com.tteam.movieland.entity.Genre;
import com.tteam.movieland.entity.Movie;
import com.tteam.movieland.service.DefaultCountryService;
import com.tteam.movieland.service.DefaultGenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Qualifier("default")
public class DefaultEnrichMovieService implements EnrichMovieService{
    private final DefaultCountryService countryService;
    private final DefaultGenreService genreService;

    @Override
    public void enrich(MovieDto movieDto, Movie updatedMovie) {
        Set<Long> countriesIds = movieDto.getCountriesId();
        Set<Country> countries = countryService.findAllById(countriesIds);
        Set<Long> genresIds = movieDto.getGenresId();
        Set<Genre> genres = genreService.findAllById(genresIds);

        updatedMovie.setCountries(countries);
        updatedMovie.setGenres(genres);
    }

    @Override
    public void enrich(Movie updatedMovie) {
        Long id = updatedMovie.getId();
        Set<Country> countries = countryService.findAllByMovieId(id);
        Set<Genre> genres = genreService.findAllByMovieId(id);
        updatedMovie.setCountries(countries);
        updatedMovie.setGenres(genres);
    }

}
