package com.tteam.movieland.repository.cache;

import java.util.Optional;

public interface Cache<T> {
    void add(Long key, T value);

    Optional<T> get(Long key);
}
