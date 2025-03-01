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

/**
 * Конфигурация сервисов погоды.
 * Загружает API-ключи из конфигурации и создает соответствующие сервисы.
 */
@Configuration
@EnableConfigurationProperties(KeyProperties.class)
@RequiredArgsConstructor
public class WeatherServiceConfiguration implements WeatherServiceManager {

    private final KeyProperties properties;
    private final Map<String, WeatherService> services = new HashMap<>();
    private final WebClient webClient;

    /**
     * Создает и настраивает сервисы погоды и кэш на основе API-ключей из конфигурации.
     *
     * @return список API-ключей и соответствующих {@link WeatherService}.
     */
    @Bean
    public Map<String, WeatherService> weatherServiceAndCacheConfiguration() {

        for (ApiKey apiKey : properties.getApiKeys()) {
            WeatherCache cache = new WeatherCache();
            WeatherService weatherService = new WeatherService(webClient, cache, apiKey);
            this.services.put(apiKey.getApiKey(), weatherService);
        }

        return this.services;
    }

    /**
     * Возвращает список всех зарегистрированных сервисов погоды.
     *
     * @return список API-ключей и соответствующих {@link WeatherService}.
     * @throws WeatherServiceInitializationException если ни один сервис не был инициализирован.
     */
    public Map<String, WeatherService> getAllServices() throws WeatherServiceInitializationException {

        if (services.isEmpty()) {
            throw new WeatherServiceInitializationException("No WeatherServices were initialized.");
        }
        return services;
    }

    /**
     * Возвращает список всех зарегистрированных сервисов погоды.
     *
     * @return список API-ключей и соответствующих {@link WeatherService}.
     * @throws WeatherServiceInitializationException если сервисы не были инициализированы.
     */
    public WeatherService getService(String apiKey) {

        if (!services.containsKey(apiKey)) {
            throw new WeatherServiceNotFoundException("WeatherService not found for API key: " + apiKey);
        }
        return services.get(apiKey);
    }

    /**
     * Удаляет сервис погоды по API-ключу.
     *
     * @param apiKey API-ключ сервиса, который нужно удалить.
     * @throws WeatherServiceNotFoundException если сервис с таким API-ключом не найден.
     */
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
