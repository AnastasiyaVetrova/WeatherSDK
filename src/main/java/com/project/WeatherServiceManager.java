package com.project;

import com.project.service.WeatherService;

import java.util.Map;

public interface WeatherServiceManager {

    WeatherService getService(String apiKey);

    Map<String, WeatherService> getAllServices();

    void removeWeatherService(String apiKey);
}
