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
import com.tteam.movieland.service.MovieService;
import com.tteam.movieland.util.CurrencyProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MovieServiceDefault implements MovieService {
    private final MovieRepository movieRepository;
    private final CountryRepository countryRepository;
    private final JpaGenreRepository genreRepository;
    private final MovieMapper mapper;

    @Override
    public List<Movie> getThreeRandom() {
        return movieRepository.findThreeRandomMovies();
    }

    @Override
    public Movie getById(Long movieId, String currency) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("Could not find movie by id: " + movieId));
        double price = CurrencyProvider.setPriceInCurrency(movie.getPrice(), currency);
        movie.setPrice(price);
        return movie;
    }

    @Override
    public List<Movie> getMoviesByGenreId(Long genreId) {
        return movieRepository.findByGenresId(PageRequest.of(0, 3), genreId).stream().toList();
    }

    @Override
    public List<Movie> getAllSortedByRating(String sortingOrder) {
        if(sortingOrder.toLowerCase().equalsIgnoreCase(Sort.Direction.DESC.name())) {
            return movieRepository.findAll(PageRequest.of(0, 5, Sort.Direction.DESC, "rating")).stream().toList();
        }else if (sortingOrder.equalsIgnoreCase(Sort.Direction.ASC.name())){
            return movieRepository.findAll(PageRequest.of(0, 5, Sort.Direction.ASC, "rating")).stream().toList();
        }else {
            return movieRepository.findAll(PageRequest.of(0, 5)).stream().toList();
        }
    }

    @Override
    public List<Movie> getMoviesByGenreSortedByRating(Long genreId, String sortingOrder) {
        if(sortingOrder.toLowerCase().equalsIgnoreCase(Sort.Direction.DESC.name())) {
            return movieRepository.findByGenresId(PageRequest.of(0, 3, Sort.Direction.DESC, "rating"), genreId).stream().toList();
        }else if (sortingOrder.equalsIgnoreCase(Sort.Direction.ASC.name())){
            return movieRepository.findByGenresId(PageRequest.of(0, 3, Sort.Direction.ASC, "rating"), genreId).stream().toList();
        }else {
            return movieRepository.findByGenresId(PageRequest.of(0, 3), genreId).stream().toList();
        }
    }

    @Override
    public MovieWithCountriesAndGenresDto saveMovieWithGenresAndCountries(MovieDto movieDto) {
        Movie movie = mapper.toMovie(movieDto);

        enrich(movieDto, movie);
        movieRepository.save(movie);
        return mapper.toWithCountriesAndGenresDto(movie);
    }

    @Override
    public MovieWithCountriesAndGenresDto updateMovieWithGenresAndCountries(Long movieId, MovieDto movieDto) {
        Movie movie = movieRepository.findById(movieId).orElseThrow(
                () -> new MovieNotFoundException("Movie was not found by provided id: " + movieId));
        Movie updatedMovie = mapper.update(movie, movieDto);

        enrich(movieDto, updatedMovie);
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
}
