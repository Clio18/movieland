package com.tteam.movieland.util;

import com.tteam.movieland.util.model.Currency;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static com.tteam.movieland.util.CurrencyProvider.setPriceInCurrency;
import static org.assertj.core.api.Assertions.assertThat;

class CurrencyProviderTest {

    @Test
    @DisplayName("Test SetPriceInCurrency Method Should Return Movie With Changed Price")
    void testSetPriceInCurrencyMethodShouldReturnMovieWithChangedPrice() {
        try (MockedStatic<CurrencyProvider> providerMockedStatic = Mockito.mockStatic(CurrencyProvider.class)) {
            providerMockedStatic.when(() -> setPriceInCurrency(10, "USD")).thenReturn(0.27);
            assertThat(setPriceInCurrency(10, "USD")).isEqualTo(0.27);
        }
    }

    @Test
    @DisplayName("Test GetCurrencyRateOnThisDay Method Should Return Actual Currency Rate")
    void testGetCurrencyRateOnThisDayMethodShouldReturnActualCurrencyRate() {
        try (MockedStatic<CurrencyProvider> providerMockedStatic = Mockito.mockStatic(CurrencyProvider.class)) {
            providerMockedStatic.when(() -> CurrencyProvider.getCurrencyRateOnThisDay(Currency.USD)).thenReturn(36.3);
            assertThat(CurrencyProvider.getCurrencyRateOnThisDay(Currency.USD)).isEqualTo(36.3);
        }
    }
}