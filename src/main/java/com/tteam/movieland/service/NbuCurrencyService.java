package com.tteam.movieland.service;

import com.tteam.movieland.client.RawCurrencyClient;
import com.tteam.movieland.service.model.Currency;
import com.tteam.movieland.service.model.RawCurrency;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NbuCurrencyService implements CurrencyService {
    private final RawCurrencyClient rawCurrencyClient;
    private volatile List<RawCurrency> currencyListFromNBU;
    private volatile Map<Integer, RawCurrency> cachedCurrency;

    @Override
    public double convert(double initPrice, Currency to) {
        return convert(initPrice, Currency.UAH, to);
    }

    @Override
    public double convert(double initPrice, Currency from, Currency to) {
        double rateFrom = 1;
        if (from != Currency.UAH) {
            rateFrom = actualRateByCurrency(from);
        }
        double rateTo = actualRateByCurrency(to);
        double rate = rateTo / rateFrom;
        return initPrice / rate;
    }

    public double actualRateByCurrency(Currency currency) {
        LocalTime timeNow = LocalTime.now();
        if (cachedCurrency == null && timeNow.isBefore(LocalTime.of(15, 30, 0))) {
            updateCurrencyCache();
        }
        return cachedCurrency.get(currency.getNumericCode()).getRate();
     }

    @PostConstruct
    @Scheduled(cron = "${cache.get.cron.currency}")
    @Retryable(retryFor = RuntimeException.class, maxAttempts = 2, backoff = @Backoff(delay = 100))
    public void getRawCurrencyListFromNBU() {
        log.info("Updating currency list from NBU API...");
        currencyListFromNBU = rawCurrencyClient.findAll();
    }

    @Scheduled(cron = "${cache.evict.cron.currency}")
    public void updateCurrencyCache() {
        log.info("Updating exchange rates cache...");
        cachedCurrency = currencyListFromNBU.stream()
                .collect(Collectors.toMap(RawCurrency::getNumericCode, Function.identity()));
    }

}
