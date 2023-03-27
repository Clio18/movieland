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
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Profile("parallel")
public class ParallelEnrichMovieService implements EnrichMovieService {
    private final CountryService countryService;
    private final GenreService genreService;
    private final ExecutorService cachedPool;

    @Override
    public void enrich(Movie movie) {
        Callable<Object> taskCountry = () -> {
            log.info("Countries enrichment started...");
            boolean isStop = Thread.currentThread().isInterrupted();
            Set<Country> countries = countryService.findAllByMovieId(movie.getId());
            if (isStop) {
                log.info("Parallel enrichment failed: movie has not been enriched with countries!");
                return new Object();
            } else {
                movie.setCountries(countries);
                log.info("Parallel enrichment done: movie enriched with countries!");
                return movie;
            }
        };

        Callable<Object> taskGenre = () -> {
            log.info("Genres enrichment started...");
            boolean isStop = Thread.currentThread().isInterrupted();
            Set<Genre> genres = genreService.findAllByMovieId(movie.getId());
            if (isStop) {
                log.info("Parallel enrichment failed: movie has not been enriched with genres!");
                return new Object();
            } else {
                movie.setGenres(genres);
                log.info("Parallel enrichment done: movie enriched with genres!");
                return movie;
            }
        };

        try {
            log.info("Parallel enrichment has been started...");
            cachedPool.invokeAll(Set.of(taskCountry, taskGenre), 5, TimeUnit.SECONDS);
            log.info("Parallel enrichment should be finished");
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
