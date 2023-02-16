package com.tteam.movieland.client;

import com.tteam.movieland.service.model.RawCurrency;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(name = "rawCurrencies", url = "${service.pathToCurrenciesRatesInJson}")
public interface RawCurrencyClient {
    @RequestMapping
    List<RawCurrency> findAll();
}
