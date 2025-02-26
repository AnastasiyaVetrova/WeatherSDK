package com.project.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponse {
    @JsonDeserialize(using = FirstWeatherDeserializer.class)
    private Weather weather;
    @JsonSetter("main")
    private Temperature temperature;
    private int visibility;
    private Wind wind;
    private long datetime = System.currentTimeMillis() / 1000;
    private Sys sys;
    @JsonProperty("timezone")
    private long timeZone;
    private String name;

    @JsonGetter("temperature")
    public Temperature getTemperature() {
        return temperature;
    }
}
