package com.project.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Set;

@ConfigurationProperties(prefix = "weather")
@Getter
@Setter
@Component
public class KeyProperties {
    private Set<ApiKey> apiKeys;
}
