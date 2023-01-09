package com.tteam.movieland.util;

import java.util.Comparator;
import java.util.List;

public enum SortAction {

    ASC {
        @Override
        public <E> List<E> perform(List<E> movies, Comparator<E> comparator) {
            return movies.stream()
                    .sorted(comparator)
                    .toList();
        }
    },

    DESC {
        @Override
        public <E> List<E> perform(List<E> movies, Comparator<E> comparator) {
            return movies.stream()
                    .sorted(comparator.reversed())
                    .toList();
        }
    };

    public abstract <E> List<E> perform(List<E> movies, Comparator<E> comparator);
}
