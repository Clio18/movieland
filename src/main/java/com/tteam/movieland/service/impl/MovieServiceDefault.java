package com.tteam.movieland.service.impl;

import com.tteam.movieland.dto.MovieDto;
import com.tteam.movieland.dto.MovieWithCountriesAndGenresDto;
import com.tteam.movieland.dto.mapper.MovieMapper;
import com.tteam.movieland.entity.Country;
import com.tteam.movieland.entity.Genre;
import com.tteam.movieland.entity.Movie;
import com.tteam.movieland.exception.MovieNotFoundException;
import com.tteam.movieland.repository.CountryRepository;
import com.tteam.movieland.repository.JpaGenreRepository;
import com.tteam.movieland.repository.MovieRepository;
import com.tteam.movieland.service.CurrencyService;
import com.tteam.movieland.service.MovieService;
import com.tteam.movieland.service.model.Currency;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class MovieServiceDefault implements MovieService {
    private final MovieRepository movieRepository;
    private final CountryRepository countryRepository;
    private final JpaGenreRepository genreRepository;
    private final CurrencyService currencyService;
    private final MovieMapper mapper;

    @Override
    public List<Movie> getThreeRandom() {
        return movieRepository.findThreeRandomMovies();
    }

    @Override
    public Movie getById(Long movieId, String currencyName) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("Could not find movie by id: " + movieId));
        Currency currency = Currency.checkCurrency(currencyName.toUpperCase());
        if (currency != Currency.UAH) {
            double price = currencyService.convert(movie.getPrice(), currency);
            movie.setPrice(price);
        }
        return movie;
    }

    @Override
    public List<Movie> getMoviesByGenreId(Long genreId) {
        return movieRepository.findByGenresId(PageRequest.of(0, 3), genreId).stream().toList();
    }

    @Override
    public List<Movie> getAllSortedByRating(String sortingOrder) {
        if (sortingOrder.toLowerCase().equalsIgnoreCase(Sort.Direction.DESC.name())) {
            return movieRepository.findAll(PageRequest.of(0, 5, Sort.Direction.DESC, "rating")).stream().toList();
        } else if (sortingOrder.equalsIgnoreCase(Sort.Direction.ASC.name())) {
            return movieRepository.findAll(PageRequest.of(0, 5, Sort.Direction.ASC, "rating")).stream().toList();
        } else {
            return movieRepository.findAll(PageRequest.of(0, 5)).stream().toList();
        }
    }

    @Override
    public List<Movie> getMoviesByGenreSortedByRating(Long genreId, String sortingOrder) {
        if (sortingOrder.toLowerCase().equalsIgnoreCase(Sort.Direction.DESC.name())) {
            return movieRepository.findByGenresId(PageRequest.of(0, 3, Sort.Direction.DESC, "rating"), genreId).stream().toList();
        } else if (sortingOrder.equalsIgnoreCase(Sort.Direction.ASC.name())) {
            return movieRepository.findByGenresId(PageRequest.of(0, 3, Sort.Direction.ASC, "rating"), genreId).stream().toList();
        } else {
            return movieRepository.findByGenresId(PageRequest.of(0, 3), genreId).stream().toList();
        }
    }

    @Override
    public MovieWithCountriesAndGenresDto saveMovieWithGenresAndCountries(MovieDto movieDto) {
        Movie movie = mapper.toMovie(movieDto);

        enrichParallel(movieDto, movie);
        movieRepository.save(movie);
        return mapper.toWithCountriesAndGenresDto(movie);
    }

    @Override
    public MovieWithCountriesAndGenresDto updateMovieWithGenresAndCountries(Long movieId, MovieDto movieDto) {
        Movie movie = movieRepository.findById(movieId).orElseThrow(
                () -> new MovieNotFoundException("Movie was not found by provided id: " + movieId));
        Movie updatedMovie = mapper.update(movie, movieDto);

        enrichParallel(movieDto, updatedMovie);
        movieRepository.save(updatedMovie);
        return mapper.toWithCountriesAndGenresDto(updatedMovie);
    }

    private void enrich(MovieDto movieDto, Movie updatedMovie) {
        Set<Long> countriesIds = movieDto.getCountriesId();
        Set<Country> countries = new HashSet<>(countryRepository.findAllById(countriesIds));
        Set<Long> genresIds = movieDto.getGenresId();
        Set<Genre> genres = new HashSet<>(genreRepository.findAllById(genresIds));

        updatedMovie.setCountries(countries);
        updatedMovie.setGenres(genres);
    }


    private void enrichParallel(MovieDto movieDto, Movie updatedMovie) {
        Callable<Object> taskCountry = () -> {
            Set<Long> countriesIds = movieDto.getCountriesId();
            HashSet<Country> countries = new HashSet<>(countryRepository.findAllById(countriesIds));
            updatedMovie.setCountries(countries);
            return updatedMovie;
        };

        Callable<Object> taskGenre = () -> {
            Set<Long> genresIds = movieDto.getGenresId();
            HashSet<Genre> genres = new HashSet<>(genreRepository.findAllById(genresIds));
            updatedMovie.setGenres(genres);
            return updatedMovie;
        };

        try (var cachedPool = Executors.newCachedThreadPool()) {
            cachedPool.invokeAll(Set.of(taskCountry, taskGenre), 5, TimeUnit.SECONDS);
        } catch (InterruptedException e){
            throw new RuntimeException("Exception from ThreadPool", e);
        }
    }
}
