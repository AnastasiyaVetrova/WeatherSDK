package com.project.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Конфигурационные свойства планировщика обновления данных о погоде.
 * <p>
 * Загружается из файла свойств с префиксом {@code weather.scheduler}.
 */
@ConfigurationProperties(prefix = "weather.scheduler")
@Getter
@Setter
@Component
public class WeatherSchedulerProperties {
    /**
     * Размер пула потоков для выполнения задач обновления погоды.
     */
    private int threadPool;
    /**
     * Интервал задержки между обновлениями погоды (в минутах).
     */
    private int timeDelay;
}
