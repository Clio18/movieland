package com.tteam.movieland.service;

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
import com.tteam.movieland.cache.SoftReferenceCache;
import com.tteam.movieland.service.model.Currency;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Supplier;

@Service
@Getter
@RequiredArgsConstructor
public class DefaultMovieService implements MovieService {

    private final MovieRepository movieRepository;
    private final CountryRepository countryRepository;
    private final JpaGenreRepository genreRepository;
    private final CurrencyService currencyService;
    private final MovieMapper mapper;
    private final SoftReferenceCache<Long, Movie> cache = new SoftReferenceCache<>();

    private final ExecutorService cachedPool = Executors.newCachedThreadPool();

    @Value("${movie.random.value}")
    private int number;

    private static final Random RANDOM = new Random();

    public List<Movie> getRandom() {

        /*The problem with select que from Question que order by RAND()
        is that your DB will order all records before return one item.
        So it's expensive in large data sets*/

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
    public MovieWithCountriesAndGenresDto saveMovieWithGenresAndCountries(MovieDto movieDto) {
        Movie movie = mapper.toMovie(movieDto);

        enrichFuture(movieDto, movie);
        movieRepository.save(movie);
        return mapper.toWithCountriesAndGenresDto(movie);
    }

    @Override
    public MovieWithCountriesAndGenresDto updateMovieWithGenresAndCountries(Long movieId, MovieDto movieDto) {
        Movie movie = getMovie(movieId, movieRepository, cache);
        Movie updatedMovie = mapper.update(movie, movieDto);
        enrichFuture(movieDto, updatedMovie);
        movieRepository.save(updatedMovie);
        return mapper.toWithCountriesAndGenresDto(updatedMovie);
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

    private Movie getMovie(Long movieId, MovieRepository movieRepository, SoftReferenceCache<Long, Movie> cache) {
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

        try {
            cachedPool.invokeAll(Set.of(taskCountry, taskGenre), 5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException("Exception from ThreadPool", e);
        }
    }

    @Async
    private CompletableFuture<List<Country>> fetchCountries(Set<Long> countriesIds) {
        return CompletableFuture.supplyAsync(() -> countryRepository.findAllById(countriesIds));
    }

    @Async
    private CompletableFuture<List<Genre>> fetchGenres(Set<Long> genresIds) {
        return CompletableFuture.supplyAsync(() -> genreRepository.findAllById(genresIds));
    }

    private void enrichFuture (MovieDto movieDto, Movie updatedMovie){
        Set<Long> countriesIds = movieDto.getCountriesId();
        Set<Long> genresIds = movieDto.getGenresId();

        CompletableFuture<List<Country>> countriesFuture = fetchCountries(countriesIds);
        CompletableFuture<List<Genre>> genresFuture = fetchGenres(genresIds);

        CompletableFuture.allOf(countriesFuture, genresFuture).join();

        try {
            Set<Country> countries = new HashSet<>(countriesFuture.get(5, TimeUnit.SECONDS));
            Set<Genre> genres = new HashSet<>(genresFuture.get(5, TimeUnit.SECONDS));

            updatedMovie.setGenres(genres);
            updatedMovie.setCountries(countries);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException("Enrichment was failed: ", e);
        }
    }

    void loadFunc(){
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
