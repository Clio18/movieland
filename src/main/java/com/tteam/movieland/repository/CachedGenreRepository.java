package com.tteam.movieland.repository;

import com.tteam.movieland.entity.Genre;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CachedGenreRepository implements GenreRepository{

    private final JpaGenreRepository jpaGenreRepository;
    private List<Genre> cachedGenreList;

    @Override
    public List<Genre> findAll() {
        log.info("Get cached genres list.");
        return cachedGenreList;
    }

    @PostConstruct
    @Scheduled(cron = "${cron.interval.genres.cache}")
    protected void updateGenresCache() {
        log.info("Updating genres cache...");
        cachedGenreList = jpaGenreRepository.findAll();
    }
}
