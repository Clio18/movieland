package com.tteam.movieland.service.model;

import com.tteam.movieland.exception.CurrencyNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Currency {
    UAH(980),
    USD(840),
    EUR(978);

    private final int numericCode;

    public static Currency checkCurrency(String currencyName) {
        return Arrays.stream(values()).map(Enum::name)
                .filter(c -> c.equals(currencyName))
                .findAny().map(Currency::valueOf)
                .orElseThrow(() -> new CurrencyNotFoundException("Currency " + currencyName + " not found."));
    }
}
