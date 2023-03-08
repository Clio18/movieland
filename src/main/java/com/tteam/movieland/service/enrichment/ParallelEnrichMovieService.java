package com.tteam.movieland.service.enrichment;

import com.tteam.movieland.dto.MovieDto;
import com.tteam.movieland.entity.Country;
import com.tteam.movieland.entity.Genre;
import com.tteam.movieland.entity.Movie;
import com.tteam.movieland.service.DefaultCountryService;
import com.tteam.movieland.service.DefaultGenreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
@Qualifier("parallel")
@Slf4j
public class ParallelEnrichMovieService implements EnrichMovieService {
    private final DefaultCountryService countryService;
    private final DefaultGenreService genreService;
    private final ExecutorService cachedPool;
    private static volatile boolean isStop = false;

    @Override
    public void enrich(MovieDto movieDto, Movie updatedMovie) {
        isStop = false;

        Callable<Object> taskCountry = () -> {
            log.info("Countries enrichment started...");
            Set<Long> countriesIds = movieDto.getCountriesId();
            //loadFunc();
            Set<Country> countries = countryService.findAllById(countriesIds);
            if (isStop) {
                log.info("Parallel enrichment failed: movie has not been enriched with countries!");
                return movieDto;
            } else {
                updatedMovie.setCountries(countries);
                log.info("Parallel enrichment done: movie enriched with countries!");
                return updatedMovie;
            }
        };

        Callable<Object> taskGenre = () -> {
            log.info("Genres enrichment started...");
            Set<Long> genresIds = movieDto.getGenresId();
            Set<Genre> genres = genreService.findAllById(genresIds);
            if (isStop) {
                log.info("Parallel enrichment failed: movie has not been enriched with genres!");
                return movieDto;
            } else {
                updatedMovie.setGenres(genres);
                log.info("Parallel enrichment done: movie enriched with genres!");
                return updatedMovie;
            }
        };

        try {
            log.info("Parallel enrichment has been started...");
            cachedPool.invokeAll(Set.of(taskCountry, taskGenre), 2, TimeUnit.SECONDS);
            isStop = true;
            log.info("Parallel enrichment should be finished");
            //loadFunc();
        } catch (InterruptedException e) {
            throw new RuntimeException("Exception from ThreadPool", e);
        }
    }

    @Override
    public void enrich(Movie updatedMovie) {
        isStop = false;

        Callable<Object> taskCountry = () -> {
            log.info("Countries enrichment started...");
            Set<Country> countries = countryService.findAllByMovieId(updatedMovie.getId());
            if (isStop) {
                log.info("Parallel enrichment failed: movie has not been enriched with countries!");
                return new Object();
            } else {
                updatedMovie.setCountries(countries);
                log.info("Parallel enrichment done: movie enriched with countries!");
                return updatedMovie;
            }
        };

        Callable<Object> taskGenre = () -> {
            log.info("Genres enrichment started...");
            Set<Genre> genres = genreService.findAllByMovieId(updatedMovie.getId());
            if (isStop) {
                log.info("Parallel enrichment failed: movie has not been enriched with genres!");
                return new Object();
            } else {
                updatedMovie.setGenres(genres);
                log.info("Parallel enrichment done: movie enriched with genres!");
                return updatedMovie;
            }
        };

        try {
            log.info("Parallel enrichment has been started...");
            cachedPool.invokeAll(Set.of(taskCountry, taskGenre), 5, TimeUnit.SECONDS);
            isStop = true;
            log.info("Parallel enrichment should be finished");
        } catch (InterruptedException e) {
            throw new RuntimeException("Exception from ThreadPool", e);
        }
    }

    //testing purposes: execution takes approx. 7 sec
    private void loadFunc() {
        int sum = 0;
        for (int i = 0; i < 100_000; i++) {
            for (int j = 0; j < 100_000; j++) {
                for (int k = 0; k < 100; k++) {
                    sum = i + j + k;
                }
            }
        }
        log.info("count {}", sum);
    }
}
