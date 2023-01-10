package com.tteam.movieland.util;

import com.tteam.movieland.exception.RandomMoviesException;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Utils {
    private static final String ASC_SORT = "ASC";
    private static final String DESC_SORT = "DESC";

    public static <E> List<E> pickNRandomElements(List<E> list, int numberOfElements, Random random) {
        int length = list.size();

        if (length < numberOfElements) throw new RandomMoviesException(String.format("""
                Cannot create list of items by provided number. Number is %1$s, total amount of items is %2$s
                """, numberOfElements, length));

        //We don't need to shuffle the whole list
        for (int i = length - 1; i >= length - numberOfElements; --i) {
            Collections.swap(list, i, random.nextInt(i + 1));
        }
        return list.subList(length - numberOfElements, length);
    }

    public static <E> List<E> pickNRandomElements(List<E> list, int numberOfElements) {
        return pickNRandomElements(list, numberOfElements, ThreadLocalRandom.current());
    }

    public static <E> List<E> performSorting(List<E> items, String sortOrder, Comparator<E> comparator) {
        String sortOrderCase = sortOrder.toUpperCase();
        if (sortOrderCase.equals(DESC_SORT)) {
            return SortAction.DESC.perform(items, comparator);
        } else if (sortOrderCase.equals(ASC_SORT)) {
            return SortAction.ASC.perform(items, comparator);
        } else return items;
    }
}
