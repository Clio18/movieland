package com.tteam.movieland.repository;

import com.tteam.movieland.entity.Genre;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CachedGenreRepository implements GenreRepository {

    private final JpaGenreRepository jpaGenreRepository;
    private List<Genre> cachedGenreList;

    @Override
    @Transactional
    public List<Genre> findAll() {
        log.info("Get cached genres list.");
        if (cachedGenreList.isEmpty()) {
            updateGenresCache();
        }
        return new ArrayList<>(cachedGenreList);
    }

    @Scheduled(fixedRateString = "${cache.evict.interval.genres}")
    void updateGenresCache() {
        log.info("Updating genres cache...");
        cachedGenreList = jpaGenreRepository.findAll();
    }
}
