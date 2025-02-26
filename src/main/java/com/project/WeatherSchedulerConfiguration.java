package com.project;

import com.project.properties.WeatherSchedulerProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(WeatherSchedulerProperties.class)
public class WeatherSchedulerConfiguration {

    private final WeatherSchedulerProperties properties;

    @Bean
    public ScheduledExecutorService scheduledExecutorService() {
        System.out.println("зашел в блок");
        return Executors.newScheduledThreadPool(properties.getThreadPool());
    }
}
