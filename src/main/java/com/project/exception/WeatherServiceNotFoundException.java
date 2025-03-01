package com.project.exception;

/**
 * Исключение, которое возникает, когда сервис погоды не найден.
 * <p>
 * Используется, если попытка получить сервис погоды с указанным API-ключом не удалась.
 */
public class WeatherServiceNotFoundException extends RuntimeException {
    public WeatherServiceNotFoundException(String message) {
        super(message);
    }
}
