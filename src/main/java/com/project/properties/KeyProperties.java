package com.project.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Конфигурационные свойства API-ключей для сервиса погоды.
 * <p>
 * Загружается из файла свойств с префиксом {@code weather}.
 */
@ConfigurationProperties(prefix = "weather")
@Getter
@Setter
@Component
public class KeyProperties {
    /**
     * Набор API-ключей, используемых для доступа к погодному API.
     */
    private Set<ApiKey> apiKeys;
}
