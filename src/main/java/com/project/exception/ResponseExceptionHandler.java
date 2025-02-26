package com.project.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@ControllerAdvice
@Slf4j
public class ResponseExceptionHandler {

    @ExceptionHandler(WebClientResponseException.class)
    private ResponseEntity<String> handleWebClientResponseException(WebClientResponseException ex) {
        log.error("WebClientResponseException", ex);
        return ResponseEntity.status(ex.getStatusCode())
                .body("Error response from server OpenWeather: " + ex.getResponseBodyAsString());
    }

    @ExceptionHandler(WebClientException.class)
    public ResponseEntity<String> handleWebClientException(WebClientException ex) {
        log.error("WebClientException", ex);
        return ResponseEntity.internalServerError()
                .body("An error occurred while trying to connect to the server." +
                        " Please check your network connection and try again.");
    }

    @ExceptionHandler(WeatherServiceInitializationException.class)
    public ResponseEntity<String> handleWeatherServiceInitializationException(WeatherServiceInitializationException ex) {
        log.error("WeatherServiceInitializationException", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Startup error: " + ex.getMessage());
    }

    @ExceptionHandler(WeatherServiceNotFoundException.class)
    public ResponseEntity<String> WeatherServiceNotFoundException(WeatherServiceNotFoundException ex) {
        log.error("WeatherServiceNotFoundException", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ex.getMessage());
    }
}
