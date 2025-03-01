package com.project;

import com.project.service.WeatherService;
import com.project.exception.WeatherServiceNotFoundException;
import com.project.exception.WeatherServiceInitializationException;

import java.util.Map;

/**
 * Интерфейс для управления сервисами погоды.
 */
public interface WeatherServiceManager {
    /**
     * Получает сервис погоды по API-ключу.
     *
     * @param apiKey API-ключ, по которому нужно найти сервис.
     * @return {@link WeatherService}, соответствующий переданному API-ключу.
     * @throws WeatherServiceNotFoundException если сервис с таким API-ключом не найден.
     */
    WeatherService getService(String apiKey);

    /**
     * Возвращает список всех зарегистрированных сервисов погоды.
     *
     * @return список API-ключей и соответствующих {@link WeatherService}.
     * @throws WeatherServiceInitializationException если сервисы не были инициализированы.
     */
    Map<String, WeatherService> getAllServices();

    /**
     * Удаляет сервис погоды по API-ключу.
     *
     * @param apiKey API-ключ сервиса, который нужно удалить.
     * @throws WeatherServiceNotFoundException если сервис с таким API-ключом не найден.
     */
    void removeWeatherService(String apiKey);
}
