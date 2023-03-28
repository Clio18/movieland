package com.tteam.movieland.cache;

import java.util.Optional;
import java.util.function.Supplier;

public interface Cache<K, T> {
    void put(K key, T value);

    Optional<T> get(K key);

    T getOrPut(K key, Supplier<T> supplier);
}
