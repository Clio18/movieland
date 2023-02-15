package com.tteam.movieland.service;

import com.tteam.movieland.service.model.Currency;

public interface CurrencyService {
    double convert(double initPrice, Currency to);
    double convert(double initPrice, Currency from, Currency to);
}
