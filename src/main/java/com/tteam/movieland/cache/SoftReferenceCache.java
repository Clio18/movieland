package com.tteam.movieland.cache;

import java.lang.ref.SoftReference;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class SoftReferenceCache<K, T> implements Cache<K, T> {
    private final ConcurrentHashMap<K, SoftReference<T>> cache = new ConcurrentHashMap<>();

    @Override
    public void add(K key, T value) {
        Objects.requireNonNull(key, "Cache doesn't support null key");
        cache.put(key, new SoftReference<>(value));
    }

    @Override
    public Optional<T> get(K key) {
        return Optional.ofNullable(cache.get(key))
                .map(SoftReference::get);
    }

    public T getOrPut(K key, Supplier<T> supplier) {
        SoftReference<T> value = cache.compute(key, (id, softReference) -> {
            if(softReference == null || softReference.get() == null){
                return new SoftReference<>(supplier.get());
            }
            return softReference;
        });

        return value.get();
    }
}


