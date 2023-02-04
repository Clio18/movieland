package com.tteam.movieland.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tteam.movieland.util.model.Currency;
import com.tteam.movieland.util.model.RawCurrency;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

@Slf4j
public class CurrencyProvider {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String PATH = "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?json";
    private static RawCurrency[] currencyListToday;

    public static double setPriceInCurrency(double initPrice, String currencyName) {
        String currencyChecked = Currency.checkCurrency(currencyName.toUpperCase());
        Currency currency = Currency.valueOf(currencyChecked);
        double rate = getCurrencyRateOnThisDay(currency);
        double price = initPrice / rate;
        return (Math.round(price * 100.0) / 100.0);
    }

    static double getCurrencyRateOnThisDay(Currency currency) {
        if (currencyListToday == null) {
            getRawCurrency();
        }
        return Arrays.stream(currencyListToday)
                .filter(c -> c.getR030() == currency.getISO4217Code())
                .map(RawCurrency::getRate)
                .findFirst()
                .orElse(1.0);
    }

    @Scheduled(cron = "${cron.interval.cache.currency}")
    static void getRawCurrency() {
        try {
            currencyListToday = objectMapper.readValue(new URL(PATH), RawCurrency[].class);
            log.info("Updating currency cache...");
        } catch (IOException e) {
            throw new RuntimeException("Cannot get currency list from url "+ PATH, e);
        }
    }

}
