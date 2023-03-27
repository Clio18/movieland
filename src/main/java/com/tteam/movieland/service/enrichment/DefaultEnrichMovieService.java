package com.tteam.movieland.service.enrichment;

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

    @Override
    public void enrich(Movie movie) {
        log.info("Default enrichment has been started...");
        Long id = movie.getId();
        Set<Country> countries = countryService.findAllByMovieId(id);
        Set<Genre> genres = genreService.findAllByMovieId(id);
        movie.setCountries(countries);
        movie.setGenres(genres);
    }

}
