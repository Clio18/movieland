package com.tteam.movieland.service;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class NbuCurrencyServiceTest {

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private ResponseEntity<Object> responseEntity;

    @InjectMocks
    private NbuCurrencyService currencyService;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
        currencyService = Mockito.spy(new NbuCurrencyService());
    }

    //TODO: can't correctly write this test ((
    @Test
    void testActualRateByCurrencyMethod() {
//        RawCurrency USD = new RawCurrency(840, "Долар США", 36.5686, "USD", "13.02.2023");
//        RawCurrency EUR = new RawCurrency(978, "Євро", 39.0644, "EUR", "13.02.2023");
//        List<RawCurrency> currencyList = List.of(USD, EUR);
//        when(restTemplate.exchange("", HttpMethod.GET, null, new ParameterizedTypeReference<>() {})).thenReturn(responseEntity);
//        when(responseEntity.getBody()).thenReturn(currencyList);
//        doNothing().when(Mockito.spy(currencyService)).updateCurrencyCache();
//        double rate = currencyService.actualRateByCurrency(Currency.USD);
//        assertEquals(36.5686, rate);
    }

}