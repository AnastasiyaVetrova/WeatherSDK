package com.project.properties;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Объект, представляющий API-ключ для сервиса погоды.
 * <p>
 * Содержит ключ, тип API и язык запросов.
 */
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ApiKey {
    /**
     * Уникальный API-ключ, используемый для запросов к погодному API.
     */
    @EqualsAndHashCode.Include
    private String apiKey;
    /**
     * Тип API, связанный с этим ключом (POLLING или ON_DEMAND).
     */
    private TypeApiEnum type;
    /**
     * Язык, используемый для запросов (например, "ru" или "en").
     */
    private String lang;
}
