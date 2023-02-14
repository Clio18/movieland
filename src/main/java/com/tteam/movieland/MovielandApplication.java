package com.tteam.movieland;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MovielandApplication {

    public static void main(String[] args) {
        SpringApplication.run(MovielandApplication.class, args);
    }

}
