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
@Profile("completableFuture")
@Slf4j
public class CompletableFutureEnrichMovieService implements EnrichMovieService {
    private final CountryService countryService;
    private final GenreService genreService;
    private final ExecutorService cachedPool;
    private final MovieMapper mapper;

    @Override
    public Movie enrich(MovieDto movieDto) {
        log.info("CompletableFuture enrichment has been started...");
        Set<Long> countriesIds = movieDto.getCountriesId();
        Set<Long> genresIds = movieDto.getGenresId();

        CompletableFuture<Set<Country>> countriesFuture;
        if (countriesIds == null) {
            Long id = movieDto.getId();
            countriesFuture = fetchCountriesByMovieId(id);
        } else {
            countriesFuture = fetchCountries(countriesIds);
        }

        CompletableFuture<Set<Genre>> genresFuture;
        if (genresIds == null) {
            Long id = movieDto.getId();
            genresFuture = fetchGenresByMovieId(id);
        } else {
            genresFuture = fetchGenres(genresIds);
        }

        CompletableFuture.allOf(countriesFuture, genresFuture).join();

        try {
            Set<Country> countries = countriesFuture.get(5, TimeUnit.SECONDS);
            Set<Genre> genres = genresFuture.get(5, TimeUnit.SECONDS);

            Movie movie = mapper.toMovie(movieDto);
            movie.setGenres(genres);
            movie.setCountries(countries);
            return movie;
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("CompletableFuture enrichment was failed: ", e);
        }
    }

    private CompletableFuture<Set<Country>> fetchCountries(Set<Long> countriesIds) {
        return CompletableFuture.supplyAsync(() -> countryService.findAllById(countriesIds), cachedPool);
    }

    private CompletableFuture<Set<Genre>> fetchGenres(Set<Long> genresIds) {
        return CompletableFuture.supplyAsync(() -> genreService.findAllById(genresIds), cachedPool);
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
