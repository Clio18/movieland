package com.tteam.movieland.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class RawCurrency {

    @JsonProperty("r030")
    private int numericCode;

    @JsonProperty("txt")
    private String nameUkr;

    private double rate;

    @JsonProperty("cc")
    private String alphabeticCode;

    @JsonProperty("exchangedate")
    private String exchangeDate;
}
