package com.project.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

@ConfigurationProperties("weather")
@Getter
@Setter
public class WeatherKeyProperties {
    private Set<ApiKey> apiKeys;
}
