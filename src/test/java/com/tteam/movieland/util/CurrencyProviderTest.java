package com.tteam.movieland.util;

import com.tteam.movieland.entity.Country;
import com.tteam.movieland.entity.Genre;
import com.tteam.movieland.entity.Movie;
import com.tteam.movieland.util.model.Currency;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.Set;

import static com.tteam.movieland.util.CurrencyProvider.setPriceInCurrency;
import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CurrencyProviderTest {

    private final Movie movie1;
    private final Movie movie2;

    public CurrencyProviderTest() {
        movie1 = Movie.builder()
                .id(1L)
                .price(10.0)
                .nameUkr("Matrix")
                .nameNative("Matrix")
                .yearOfRelease(1989)
                .rating(10.0)
                .poster("url/")
                .description("Best movie")
                .countries(Set.of(Country.builder()
                        .countryName("usa")
                        .build()))
                .genres(Set.of(Genre.builder()
                        .genreName("comedy")
                        .build()))
                .build();

        movie2 = Movie.builder()
                .id(1L)
                .price(0.27)
                .nameUkr("Matrix")
                .nameNative("Matrix")
                .yearOfRelease(1989)
                .rating(10.0)
                .poster("url/")
                .description("Best movie")
                .countries(Set.of(Country.builder()
                        .countryName("usa")
                        .build()))
                .genres(Set.of(Genre.builder()
                        .genreName("comedy")
                        .build()))
                .build();
    }

    @Test
    @DisplayName("Test SetPriceInCurrency Method Should Return Movie With Changed Price")
    void testSetPriceInCurrencyMethodShouldReturnMovieWithChangedPrice() {
        try (MockedStatic<CurrencyProvider> providerMockedStatic = Mockito.mockStatic(CurrencyProvider.class)) {
            providerMockedStatic.when(() -> setPriceInCurrency(movie1, "USD")).thenReturn(movie2);
            assertThat(setPriceInCurrency(movie1, "USD")).isEqualTo(movie2);
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