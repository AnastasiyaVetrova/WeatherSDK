package com.project;

import com.project.exception.ResponseExceptionHandler;
import com.project.properties.WeatherSchedulerProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(WeatherSchedulerProperties.class)
public class WeatherStarterConfiguration {
    private final WeatherSchedulerProperties properties;

    @Bean
    public ScheduledExecutorService scheduledExecutorService() {
        return Executors.newScheduledThreadPool(properties.getThreadPool());
    }

    @Bean
    public ResponseExceptionHandler responseExceptionHandler() {
        return new ResponseExceptionHandler();
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/weather")
                .build();
    }
}
