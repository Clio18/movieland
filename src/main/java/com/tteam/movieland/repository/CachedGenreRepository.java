package com.tteam.movieland.repository;

import com.tteam.movieland.entity.Genre;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CachedGenreRepository implements GenreRepository {
    private final JpaGenreRepository jpaGenreRepository;
    private volatile List<Genre> cachedGenreList;
    private final ReentrantLock lock = new ReentrantLock();

    @Override
    public List<Genre> findAll() {
        if (cachedGenreList == null || cachedGenreList.isEmpty()) {
            updateGenresCache();
        }
        lock.lock();
        try {
            log.info("Get cached genres list.");
            return new ArrayList<>(cachedGenreList);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Set<Genre> findAllById(Set<Long> genresIds) {
        return new HashSet<>(jpaGenreRepository.findAllById(genresIds));
    }

    @Override
    public Set<Genre> findAllByMovieId(Long id) {
        return jpaGenreRepository.findAllByMovieId(id);
    }

    @PostConstruct
    @Scheduled(initialDelayString = "${cache.evict.interval.genres}",
            fixedDelayString = "${cache.evict.interval.genres}", timeUnit = TimeUnit.HOURS)
    void updateGenresCache() {
        lock.lock();
        try {
            log.info("Updating genres cache...");
            cachedGenreList = jpaGenreRepository.findAll();
        } finally {
            lock.unlock();
        }
    }
}
