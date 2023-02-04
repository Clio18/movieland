package com.tteam.movieland.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Setter
@Getter
@Configuration
@EnableScheduling
@ConfigurationProperties(prefix = "application")
public class ApplicationConfig {

    private String jwtSecretKey;
    private String jwTokenPrefix;
    private Integer jwTokenExpirationAfterHours;
    private String pathToCurrenciesRatesInJson;

}
