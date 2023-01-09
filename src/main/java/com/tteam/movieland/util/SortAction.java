package com.tteam.movieland.util;

import com.tteam.movieland.entity.Movie;

import java.util.Comparator;
import java.util.List;

public enum SortAction {

    ASC_RATING {
        @Override
        public List<Movie> perform(List<Movie> movies) {
           return movies.stream()
                   .sorted(Comparator.comparing(Movie::getRating))
                   .toList();
        }
    },

    DESC_RATING {
        @Override
        public List<Movie> perform(List<Movie> movies) {
            return movies.stream()
                    .sorted(Comparator.comparing(Movie::getRating).reversed())
                    .toList();
        }
    };

    public abstract List<Movie> perform(List<Movie> movies);
}
