package com.tteam.movieland.service;

import com.tteam.movieland.client.RawCurrencyClient;
import com.tteam.movieland.service.model.RawCurrency;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NbuCurrencyServiceTest {

    @Mock
    private RawCurrencyClient rawCurrencyClient;;
    @InjectMocks
    private NbuCurrencyService currencyService;

    @Test
    @DisplayName("Test Update Currency Cache")
    void testUpdateCurrencyCache() {
        RawCurrency USD = new RawCurrency(840, "Долар США", 36.5686, "USD", "13.02.2023");
        RawCurrency EUR = new RawCurrency(978, "Євро", 39.0644, "EUR", "13.02.2023");
        List<RawCurrency> currencyList = List.of(USD, EUR);
        when(rawCurrencyClient.findAll()).thenReturn(currencyList);
        currencyService.updateCurrencyCache();
        verify(rawCurrencyClient).findAll();
    }

}