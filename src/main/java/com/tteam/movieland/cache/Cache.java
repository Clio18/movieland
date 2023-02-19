package com.tteam.movieland.cache;

import java.util.Optional;

public interface Cache<K, T> {
    void add(K key, T value);

    Optional<T> get(K key);
}
