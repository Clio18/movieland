package com.tteam.movieland;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableRetry
@EnableFeignClients
public class MovielandApplication {

    public static void main(String[] args) {
        SpringApplication.run(MovielandApplication.class, args);
    }

}
