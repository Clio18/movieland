package com.tteam.movieland.util;

import com.tteam.movieland.exception.RandomMoviesException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {
    private List <Integer> list = getSampleList();
    private final Integer n = 5;

    @Test
    @DisplayName("test PickNRandomElements And CheckNotNull, Not Empty, Size and Result Not Sorted")
    void testPickNRandomElements_CheckNotNull_NotEmpty_Size_NotSorted(){
        List<Integer> elements = Utils.pickNRandomElements(list, n);
        assertNotNull(elements);
        assertFalse(elements.isEmpty());
        assertEquals(n, elements.size());
        List<Integer> sorted = elements.stream().sorted().collect(Collectors.toList());
        assertNotEquals(elements, sorted);
    }

    @Test
    @DisplayName("test PickNRandomElements And Check Exception Throwing And Message")
    void testPickNRandomElements_CheckExceptionThrowingAndMessage(){
        int bigNumber = 100;
        RandomMoviesException exception = assertThrows(RandomMoviesException.class, () -> {
            Utils.pickNRandomElements(list, bigNumber);
        });
        String message = String.format("""
        Cannot create list of movies by provided number. Number is %1$s, total amount of movies is %2$s
        """, bigNumber, list.size());
        assertEquals(message, exception.getMessage());
    }

    private List<Integer> getSampleList() {
        List <Integer> list = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            list.add(i);
        }
        return list;
    }

}