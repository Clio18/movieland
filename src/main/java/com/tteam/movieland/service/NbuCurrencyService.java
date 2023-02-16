package com.tteam.movieland.service;

import com.tteam.movieland.client.RawCurrencyClient;
import com.tteam.movieland.service.model.Currency;
import com.tteam.movieland.service.model.RawCurrency;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class NbuCurrencyService implements CurrencyService {

    @Autowired
    RawCurrencyClient rawCurrencyClient;

    private volatile List<RawCurrency> currencyList;


    @Override
    public double convert(double initPrice, Currency to) {
        return convert(initPrice, Currency.UAH, to);
    }

    @Override
    public double convert(double initPrice, Currency from, Currency to) {
        double rateFrom = actualRateByCurrency(from);
        double rateTo = actualRateByCurrency(to);
        double rate = rateTo / rateFrom;
        return initPrice / rate;
    }

    public double actualRateByCurrency(Currency currency) {
        if (currencyList == null || currencyList.isEmpty()) {
            updateCurrencyCache();
        }
         return currencyList.stream()
                 .filter(c -> c.getNumericCode() == currency.getNumericCode())
                 .map(RawCurrency::getRate)
                 .findFirst()
                 .orElse(1.0);
     }

    @PostConstruct
    @Scheduled(cron = "${cache.evict.cron.currency}")
    @Retryable(retryFor = RuntimeException.class, maxAttempts = 2, backoff = @Backoff(delay = 100))
    public void updateCurrencyCache() {
        log.info("Updating exchange rates cache...");
        currencyList = rawCurrencyClient.findAll();
    }

}
