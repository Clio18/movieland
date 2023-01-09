package com.tteam.movieland.service;

import com.tteam.movieland.entity.Genre;
import com.tteam.movieland.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Oleksandr Shevchenko
 */
@Log4j2
@Service
@EnableScheduling
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private List<Genre> genresCache = new ArrayList<>();

    @Override
    public List<Genre> getAll() {
        if (genresCache.isEmpty()) {
            genresCache = genreRepository.findAll();
            log.info("Get genres from DB");
        } else {
            log.info("Get genres from cache");
        }
        return genresCache;
    }

    @Scheduled(cron = "${cron.interval}")
    private void clearCache() {
        log.info("Clearing genres cache...");
        genresCache = new ArrayList<>();
    }
}
