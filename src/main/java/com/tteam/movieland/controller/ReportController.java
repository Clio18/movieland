package com.tteam.movieland.controller;

import com.tteam.movieland.config.MyKafkaConsumerFactory;
import com.tteam.movieland.dto.MovieWithCountriesAndGenresDto;
import com.tteam.movieland.dto.mapper.MovieMapper;
import com.tteam.movieland.entity.Movie;
import com.tteam.movieland.entity.kafka.Report;
import com.tteam.movieland.entity.kafka.ReportRequest;
import com.tteam.movieland.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/report/", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ReportController {

    private final MovieMapper mapper;
    private final MovieService movieService;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final MyKafkaConsumerFactory kafkaConsumerFactory;

    private static final String HEADER_ATTACH = "attachment; filename=\"";


    @PostMapping("allmovies")
    protected ResponseEntity<byte[]> publish(@RequestBody ReportRequest request) throws InterruptedException {
        List<Movie> movies = movieService.getAllSortedByRating("");
        List<MovieWithCountriesAndGenresDto> moviesList = movies.stream().map(mapper::toWithCountriesAndGenresDto).toList();
        Report<MovieWithCountriesAndGenresDto> moviesReport = new Report<>(request, moviesList);
        kafkaTemplate.send("allMovies", moviesReport);

        KafkaConsumer<String, byte[]> consumer = kafkaConsumerFactory.getConsumer();

        while (true) {
            ConsumerRecords<String, byte[]> records = consumer.poll(Duration.ofMillis(100));
            if (records.records("allMoviesRequest").iterator().hasNext()) {
                List<byte[]> allRecords = new ArrayList<>();
                for (ConsumerRecord<String, byte[]> record : records.records("allMoviesRequest")) {
                    allRecords.add(record.value());
                }
                String filename = String.format("All_movies_report_%s.xlsx", LocalDate.now());
                consumer.close();
                return ResponseEntity
                        .ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, HEADER_ATTACH + filename + "\"")
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(allRecords.get(0));
            }
        }

    }
}