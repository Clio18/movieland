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
@Profile("completableFuture")
@Slf4j
public class CompletableFutureEnrichMovieService implements EnrichMovieService {
    private final CountryService countryService;
    private final GenreService genreService;
    private final ExecutorService cachedPool;

    @Override
    public void enrich(Movie movie) {
        log.info("CompletableFuture enrichment has been started...");
        Long id = movie.getId();

        CompletableFuture<Set<Country>> countriesFuture = fetchCountriesByMovieId(id);
        CompletableFuture<Set<Genre>> genresFuture = fetchGenresByMovieId(id);

        CompletableFuture.allOf(countriesFuture, genresFuture).join();

        try {
            Set<Country> countries = countriesFuture.get(5, TimeUnit.SECONDS);
            Set<Genre> genres = genresFuture.get(5, TimeUnit.SECONDS);

            movie.setGenres(genres);
            movie.setCountries(countries);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("CompletableFuture enrichment was failed: ", e);
        }

    }

    private CompletableFuture<Set<Country>> fetchCountriesByMovieId(Long id) {
        return CompletableFuture.supplyAsync(() -> countryService.findAllByMovieId(id), cachedPool);
    }

    private CompletableFuture<Set<Genre>> fetchGenresByMovieId(Long id) {
        return CompletableFuture.supplyAsync(() -> genreService.findAllByMovieId(id), cachedPool);
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
        System.out.println(sum);
    }
}
