package com.project.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/**
 * Обработчик исключений для обработки ошибок, связанных с WebClient
 * и работой сервиса погоды.
 * <p>
 * Перехватывает исключения и возвращает корректные HTTP-ответы с пояснениями.
 */
@ControllerAdvice
@Slf4j
public class ResponseExceptionHandler {
    /**
     * Обрабатывает исключение {@link WebClientResponseException}, возникающее при получении
     * ошибки от сервера OpenWeather.
     *
     * @param ex Исключение WebClientResponseException.
     * @return ResponseEntity с HTTP-статусом ошибки и сообщением.
     */
    @ExceptionHandler(WebClientResponseException.class)
    private ResponseEntity<String> handleWebClientResponseException(WebClientResponseException ex) {
        log.error("WebClientResponseException", ex);
        return ResponseEntity.status(ex.getStatusCode())
                .body("Error response from server OpenWeather: " + ex.getResponseBodyAsString());
    }

    /**
     * Обрабатывает исключение {@link WebClientException}, возникающее при проблемах с подключением.
     *
     * @param ex Исключение WebClientException.
     * @return ResponseEntity с HTTP 500 и сообщением об ошибке сети.
     */
    @ExceptionHandler(WebClientException.class)
    public ResponseEntity<String> handleWebClientException(WebClientException ex) {
        log.error("WebClientException", ex);
        return ResponseEntity.internalServerError()
                .body("An error occurred while trying to connect to the server." +
                        " Please check your network connection and try again.");
    }

    /**
     * Обрабатывает исключение {@link WeatherServiceInitializationException}, возникающее
     * при ошибке инициализации сервиса погоды.
     *
     * @param ex Исключение WeatherServiceInitializationException.
     * @return ResponseEntity с HTTP 500 и сообщением об ошибке инициализации.
     */
    @ExceptionHandler(WeatherServiceInitializationException.class)
    public ResponseEntity<String> handleWeatherServiceInitializationException(WeatherServiceInitializationException ex) {
        log.error("WeatherServiceInitializationException", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Startup error: " + ex.getMessage());
    }

    /**
     * Обрабатывает исключение {@link WeatherServiceNotFoundException}, если сервис погоды не найден.
     *
     * @param ex Исключение WeatherServiceNotFoundException.
     * @return ResponseEntity с HTTP 500 и сообщением об отсутствии сервиса.
     */
    @ExceptionHandler(WeatherServiceNotFoundException.class)
    public ResponseEntity<String> WeatherServiceNotFoundException(WeatherServiceNotFoundException ex) {
        log.error("WeatherServiceNotFoundException", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ex.getMessage());
    }
}
