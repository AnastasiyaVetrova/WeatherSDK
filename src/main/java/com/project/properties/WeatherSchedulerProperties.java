package com.project.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("weather.scheduler")
@Getter
@Setter
public class WeatherSchedulerProperties {
    private int threadPool;
    private int timeDelay;
}
