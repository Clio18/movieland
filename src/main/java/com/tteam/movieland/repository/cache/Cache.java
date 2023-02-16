package com.tteam.movieland.repository.cache;

public interface Cache<T> {
    void add(Long key, T value);

    T get(Long key);
}
