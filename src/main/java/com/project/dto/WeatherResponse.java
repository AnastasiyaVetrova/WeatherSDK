package com.project.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class WeatherResponse {
    @JsonDeserialize(using = FirstWeatherDeserializer.class)
    private Weather weather;
    @JsonSetter("main")
    private Temperature temperature;
    private int visibility;
    private Wind wind;
    @JsonSetter("dt")
    private long dateTime;
    private Sys sys;
    @JsonProperty("timezone")
    private long timeZone;
    private String name;
    @JsonIgnore
    private long timeNow = Instant.now().getEpochSecond();

    @JsonGetter("temperature")
    public Temperature getTemperature() {
        return temperature;
    }

    @JsonGetter("datetime")
    public long getDateTime() {
        return dateTime;
    }
}
