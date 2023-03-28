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
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Profile("parallel")
public class ParallelEnrichMovieService implements EnrichMovieService {
    private final CountryService countryService;
    private final GenreService genreService;
    private final ExecutorService cachedPool;
    private final MovieMapper mapper;

    @Override
    public Movie enrich(MovieDto movieDto) {
        Movie movie = mapper.toMovie(movieDto);
        Callable<Object> taskCountry = () -> {
            log.info("Countries enrichment started...");
            Set<Long> countriesIds = movieDto.getCountriesId();
            //loadFunc();
            Set<Country> countries;
            if (countriesIds == null) {
                Long id = movieDto.getId();
                countries = countryService.findAllByMovieId(id);
            } else {
                countries = countryService.findAllById(countriesIds);
            }

            boolean isStop = Thread.currentThread().isInterrupted();
            if (isStop) {
                log.info("Parallel enrichment failed: movie has not been enriched with countries!");
                return movieDto;
            } else {
                movie.setCountries(countries);
                log.info("Parallel enrichment done: movie enriched with countries!");
                return movie;
            }
        };

        Callable<Object> taskGenre = () -> {
            log.info("Genres enrichment started...");
            Set<Long> genresIds = movieDto.getGenresId();
            Set<Genre> genres;
            if (genresIds == null) {
                Long id = movieDto.getId();
                genres = genreService.findAllByMovieId(id);
            } else {
                genres = genreService.findAllById(genresIds);
            }
            boolean isStop = Thread.currentThread().isInterrupted();
            if (isStop) {
                log.info("Parallel enrichment failed: movie has not been enriched with genres!");
                return movieDto;
            } else {
                movie.setGenres(genres);
                log.info("Parallel enrichment done: movie enriched with genres!");
                return movie;
            }
        };

        try {
            log.info("Parallel enrichment has been started...");
            cachedPool.invokeAll(Set.of(taskCountry, taskGenre), 2, TimeUnit.SECONDS);
            log.info("Parallel enrichment should be finished");
            //loadFunc();
            return movie;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
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
