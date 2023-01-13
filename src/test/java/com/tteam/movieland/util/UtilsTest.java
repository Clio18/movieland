package com.tteam.movieland.util;

import com.tteam.movieland.entity.Movie;
import com.tteam.movieland.exception.RandomMoviesException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {
    private List <Integer> list = getSampleList();
    private final Integer numberOfElements = 5;

    @Test
    @DisplayName("Test PickNRandomElements And CheckNotNull, Not Empty, Size and Result Not Sorted")
    void testPickNRandomElements_CheckNotNull_NotEmpty_Size_NotSorted(){
        List<Integer> elements = Utils.pickNRandomElements(list, numberOfElements);
        assertNotNull(elements);
        assertFalse(elements.isEmpty());
        assertEquals(numberOfElements, elements.size());
        List<Integer> sorted = elements.stream().sorted().collect(Collectors.toList());
        assertNotEquals(elements, sorted);
    }

    @Test
    @DisplayName("Test PickNRandomElements And Check Exception Throwing And Message")
    void testPickNRandomElements_CheckExceptionThrowingAndMessage(){
        int bigNumber = 100;
        RandomMoviesException exception = assertThrows(RandomMoviesException.class, () -> {
            Utils.pickNRandomElements(list, bigNumber);
        });
        String message = String.format("""
        Cannot create list of items by provided number. Number is %1$s, total amount of items is %2$s
        """, bigNumber, list.size());
        assertEquals(message, exception.getMessage());
    }

    @Test
    @DisplayName("Test PerformSorting With Ascending Order And Check Order Of The Items")
    void testPerformSortingAscOrder_AndCheckOrderOfTheItems (){
        List<Movie> movies = List.of(
                Movie.builder().rating(2.0).build(),
                Movie.builder().rating(1.0).build(),
                Movie.builder().rating(3.0).build()
        );
        Comparator<Movie> comparator = Comparator.comparing(Movie::getRating);
        List<Movie> sortedMovies = Utils.performSorting(movies, "asc", comparator);
        assertEquals(1.0, sortedMovies.get(0).getRating());
        assertEquals(2.0, sortedMovies.get(1).getRating());
        assertEquals(3.0, sortedMovies.get(2).getRating());
    }

    @Test
    @DisplayName("Test PerformSorting With Descending Order And Check Order Of The Items")
    void testPerformSortingDescOrder_AndCheckOrderOfTheItems (){
        List<Movie> movies = List.of(
                Movie.builder().rating(2.0).build(),
                Movie.builder().rating(1.0).build(),
                Movie.builder().rating(3.0).build()
        );
        Comparator<Movie> comparator = Comparator.comparing(Movie::getRating);
        List<Movie> sortedMovies = Utils.performSorting(movies, "desc", comparator);
        assertEquals(1.0, sortedMovies.get(2).getRating());
        assertEquals(2.0, sortedMovies.get(1).getRating());
        assertEquals(3.0, sortedMovies.get(0).getRating());
    }

    @Test
    @DisplayName("Test PerformSorting With Wrong Order And Check Order Don't Change")
    void testPerformSortingWrongOrder_AndCheckOrderDontChange (){
        List<Movie> movies = List.of(
                Movie.builder().rating(2.0).build(),
                Movie.builder().rating(1.0).build(),
                Movie.builder().rating(3.0).build()
        );
        Comparator<Movie> comparator = Comparator.comparing(Movie::getRating);
        List<Movie> sortedMovies = Utils.performSorting(movies, "xxx", comparator);
        assertEquals(movies, sortedMovies);
    }

    private List<Integer> getSampleList() {
        List <Integer> list = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            list.add(i);
        }
        return list;
    }

}