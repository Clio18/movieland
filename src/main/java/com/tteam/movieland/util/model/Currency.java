package com.tteam.movieland.util.model;

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

    private final int ISO4217Code;

    public static String checkCurrency(String currencyName) {
        return Arrays.stream(values()).map(Enum::name)
                .filter(c -> c.equals(currencyName))
                .findAny()
                .orElseThrow(() -> new CurrencyNotFoundException("Currency " + currencyName + " not found."));
    }
}
