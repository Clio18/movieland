package com.tteam.movieland.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tteam.movieland.entity.Movie;
import com.tteam.movieland.util.model.Currency;
import com.tteam.movieland.util.model.RawCurrency;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

public class CurrencyProvider {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String pathToCurrenciesRatesInJson = "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?json";

    public static Movie setPriceInCurrency(Movie movie, String currencyName) {
        String currencyChecked = Currency.checkCurrency(currencyName.toUpperCase());
        Currency currency = Currency.valueOf(currencyChecked);
        double rate = getCurrencyRateOnThisDay(currency);
        double price = movie.getPrice() / rate;
        double priceRound = (Math.round(price * 100.0) / 100.0);
        movie.setPrice(priceRound);
        return movie;
    }

    static double getCurrencyRateOnThisDay(Currency currency) {
        try {
            RawCurrency[] rawCurrencyList = objectMapper.readValue(new URL(pathToCurrenciesRatesInJson), RawCurrency[].class);
            return Arrays.stream(rawCurrencyList)
                    .filter(c -> c.getR030() == currency.getISO4217Code())
                    .map(RawCurrency::getRate)
                    .findFirst()
                    .orElse(1.0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
