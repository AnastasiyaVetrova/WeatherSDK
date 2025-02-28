package com.project;

import com.project.exception.WeatherServiceInitializationException;
import com.project.exception.WeatherServiceNotFoundException;
import com.project.properties.ApiKey;
import com.project.properties.KeyProperties;
import com.project.service.WeatherCache;
import com.project.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(KeyProperties.class)
@RequiredArgsConstructor
public class WeatherServiceConfiguration implements WeatherServiceManager {

    private final KeyProperties properties;
    private final Map<String, WeatherService> services = new HashMap<>();
    private final WebClient webClient;

    @Bean
    public Map<String, WeatherService> weatherCacheConfiguration() {

        for (ApiKey apiKey : properties.getApiKeys()) {
            WeatherCache cache = new WeatherCache();
            WeatherService weatherService = new WeatherService(webClient, cache, apiKey);
            this.services.put(apiKey.getApiKey(), weatherService);
        }

        return this.services;
    }

    public Map<String, WeatherService> getAllServices() throws WeatherServiceInitializationException {

        if (services.isEmpty()) {
            throw new WeatherServiceInitializationException("No WeatherServices were initialized.");
        }
        return services;
    }

    public WeatherService getService(String apiKey) {

        if (!services.containsKey(apiKey)) {
            throw new WeatherServiceNotFoundException("WeatherService not found for API key: " + apiKey);
        }
        return services.get(apiKey);
    }

    public void removeWeatherService(String apiKey) {
        WeatherService weatherService = services.get(apiKey);
        if (weatherService != null) {
            weatherService.clearCache();
            services.remove(apiKey);
        } else {
            throw new WeatherServiceNotFoundException("WeatherService not found for API key: " + apiKey);
        }
    }
}
