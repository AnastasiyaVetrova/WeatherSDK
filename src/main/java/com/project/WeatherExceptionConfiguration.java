package com.project;

import com.project.exception.ResponseExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WeatherExceptionConfiguration {

    @Bean
    public ResponseExceptionHandler responseExceptionHandler() {
        return new ResponseExceptionHandler();
    }
}
