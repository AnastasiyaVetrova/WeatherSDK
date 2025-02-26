package com.project.exception;

public class WeatherServiceNotFoundException extends RuntimeException {
    public WeatherServiceNotFoundException(String message) {
        super(message);
    }
}
