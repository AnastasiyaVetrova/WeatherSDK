package com.project.exception;

public class WeatherServiceInitializationException extends RuntimeException {
    public WeatherServiceInitializationException(String message) {
        super(message);
    }
}
