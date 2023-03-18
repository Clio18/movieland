package com.tteam.movieland.service.enrichment;

import com.tteam.movieland.dto.MovieDto;
import com.tteam.movieland.entity.Country;
import com.tteam.movieland.entity.Genre;
import com.tteam.movieland.entity.Movie;
import com.tteam.movieland.service.DefaultCountryService;
import com.tteam.movieland.service.DefaultGenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
@Qualifier("future")
public class FutureEnrichMovieService implements EnrichMovieService{
    private final DefaultCountryService countryService;
    private final DefaultGenreService genreService;

    private final ExecutorService cachedPool;

    @Async
    private CompletableFuture<Set<Country>> fetchCountries(Set<Long> countriesIds) {
        return CompletableFuture.supplyAsync(() -> countryService.findAllById(countriesIds), cachedPool);
    }

    @Async
    private CompletableFuture<Set<Genre>> fetchGenres(Set<Long> genresIds) {
        return CompletableFuture.supplyAsync(() -> genreService.findAllById(genresIds), cachedPool);
    }

    @Async
    private CompletableFuture<Set<Country>> fetchCountriesByMovieId(Long id) {
        return CompletableFuture.supplyAsync(() -> countryService.findAllByMovieId(id), cachedPool);
    }

    @Async
    private CompletableFuture<Set<Genre>> fetchGenresByMovieId(Long id) {
        return CompletableFuture.supplyAsync(() -> genreService.findAllByMovieId(id), cachedPool);
    }
    @Override
    public void enrich(MovieDto movieDto, Movie updatedMovie) {
        Set<Long> countriesIds = movieDto.getCountriesId();
        Set<Long> genresIds = movieDto.getGenresId();

        CompletableFuture<Set<Country>> countriesFuture = fetchCountries(countriesIds);
        CompletableFuture<Set<Genre>> genresFuture = fetchGenres(genresIds);

        CompletableFuture.allOf(countriesFuture, genresFuture).join();

        try {
            Set<Country> countries = countriesFuture.get(5, TimeUnit.SECONDS);
            Set<Genre> genres = genresFuture.get(5, TimeUnit.SECONDS);

            updatedMovie.setGenres(genres);
            updatedMovie.setCountries(countries);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Enrichment was failed: ", e);
        }
    }

    @Override
    public void enrich(Movie updatedMovie) {
        Long id = updatedMovie.getId();

        CompletableFuture<Set<Country>> countriesFuture = fetchCountriesByMovieId(id);
        CompletableFuture<Set<Genre>> genresFuture = fetchGenresByMovieId(id);

        CompletableFuture.allOf(countriesFuture, genresFuture).join();

        try {
            Set<Country> countries = countriesFuture.get(5, TimeUnit.SECONDS);
            Set<Genre> genres = genresFuture.get(5, TimeUnit.SECONDS);

            updatedMovie.setGenres(genres);
            updatedMovie.setCountries(countries);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Enrichment was failed: ", e);
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
        System.out.println(sum);
    }
}
