package com.tteam.movieland.util.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class RawCurrency {

    @JsonProperty("r030")
    private int r030;

    @JsonProperty("txt")
    private String txt;

    @JsonProperty("rate")
    private double rate;

    @JsonProperty("cc")
    private String cc;

    @JsonProperty("exchangedate")
    private String exchangeDate;
}
