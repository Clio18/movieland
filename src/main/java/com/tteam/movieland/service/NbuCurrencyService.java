package com.tteam.movieland.service;

import com.tteam.movieland.service.model.Currency;
import com.tteam.movieland.service.model.RawCurrency;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Service
public class NbuCurrencyService implements CurrencyService {
    @Value("${service.pathToCurrenciesRatesInJson}")
    private String PATH;
    private List<RawCurrency> currencyList;


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
    public void updateCurrencyCache() {
        ResponseEntity<List<RawCurrency>> responseEntity = new RestTemplate().exchange(
                PATH, HttpMethod.GET, null, new ParameterizedTypeReference<>() {}
        );
        log.info("Updating exchange rates cache...");
        currencyList = responseEntity.getBody();
    }

}
