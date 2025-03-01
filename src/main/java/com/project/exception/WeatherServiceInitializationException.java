package com.project.exception;

/**
 * Исключение, которое возникает при ошибке инициализации сервиса погоды.
 * <p>
 * Используется, когда сервис погоды не может быть инициализирован должным образом
 * в процессе запуска приложения.
 */
public class WeatherServiceInitializationException extends RuntimeException {
    public WeatherServiceInitializationException(String message) {
        super(message);
    }
}
