package com.project.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "weather.scheduler")
@Getter
@Setter
@Component
public class WeatherSchedulerProperties {
    private int threadPool;
    private int timeDelay;
}
