package com.tteam.movieland.repository.cache;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.ref.SoftReference;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Scope("prototype")
public class InMemoryCache<T> implements Cache<T> {
    private final ConcurrentHashMap<Long, SoftReference<T>> cache = new ConcurrentHashMap<>();

    @Override
    public void add(Long key, T value) {
        if (key == null) {
            return;
        }
        if (value == null) {
            cache.remove(key);
        } else {
            cache.put(key, new SoftReference<>(value));
        }
    }

    @Override
    public Optional<T> get(Long key) {
        return Optional.ofNullable(cache.get(key))
                .map(SoftReference::get);
    }
}


