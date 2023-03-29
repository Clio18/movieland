package com.tteam.movieland.service;

import com.tteam.movieland.cache.Cache;
import com.tteam.movieland.dto.MovieDto;
import com.tteam.movieland.entity.Movie;
import com.tteam.movieland.exception.MovieNotFoundException;
import com.tteam.movieland.repository.MovieRepository;
import com.tteam.movieland.cache.SoftReferenceCache;
import com.tteam.movieland.service.enrichment.EnrichMovieService;
import com.tteam.movieland.service.model.Currency;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Supplier;

@Service
@Getter
@RequiredArgsConstructor
public class DefaultMovieService implements MovieService {
    private final EnrichMovieService enrichMovieService;
    private final MovieRepository movieRepository;
    private final CurrencyService currencyService;
    private final Cache<Long, Movie> cache = new SoftReferenceCache<>();

    @Value("${movie.random.value}")
    private int number;

    private static final Random RANDOM = new Random();

    public List<Movie> getRandom() {

        /*The problem with select que from Question que order by RAND()
        is that your DB will order all records before return one item.
        So it's expensive in large data sets
        https://stackoverflow.com/questions/24279186/fetch-random-records-using-spring-data-jpa
        */

        //to start from 0
        long count = movieRepository.count() - 1;
        long range = count / number;
        long index = RANDOM.nextLong((range));
        return movieRepository.findAll(PageRequest.of((int) index, number)).stream().toList();
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
    public Movie saveMovieWithGenresAndCountries(MovieDto movieDto) {
        Movie movie = enrichMovieService.enrich(movieDto);
        movieRepository.save(movie);
        return movie;
    }

    @Override
    public Movie updateMovieWithGenresAndCountries(Long movieId, MovieDto movieDto) {
        movieDto.setId(movieId);
        Movie movie = enrichMovieService.enrich(movieDto);
        movieRepository.save(movie);
        Optional<Movie> optionalMovie = cache.get(movieId);
        if (optionalMovie.isPresent()) {
            cache.put(movieId, movie);
        }
        return movie;
    }

    @Override
    public Movie getById(Long movieId, String currencyName) {
        Movie movie = getMovie(movieId, movieRepository, cache);
        Currency currency = Currency.checkCurrency(currencyName.toUpperCase());
        if (currency != Currency.UAH) {
            double price = currencyService.convert(movie.getPrice(), currency);
            movie.setPrice(price);
        }
        return movie;
    }

    private Movie getMovie(Long movieId, MovieRepository movieRepository, Cache<Long, Movie> cache) {
        Supplier<Movie> supplier = () -> movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("Could not find movie by id: " + movieId));
        Movie movie = cache.getOrPut(movieId, supplier);
        return Movie.builder()
                .id(movie.getId())
                .nameUkr(movie.getNameUkr())
                .nameNative(movie.getNameNative())
                .yearOfRelease(movie.getYearOfRelease())
                .description(movie.getDescription())
                .price(movie.getPrice())
                .rating(movie.getRating())
                .countries(movie.getCountries())
                .poster(movie.getPoster())
                .genres(movie.getGenres())
                .reviews(movie.getReviews())
                .build();
    }
}
