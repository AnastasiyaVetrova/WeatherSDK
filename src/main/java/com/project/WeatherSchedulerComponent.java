package com.project;

import com.project.properties.TypeApiEnum;
import com.project.properties.WeatherSchedulerProperties;
import com.project.service.WeatherService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Компонент, отвечающий за запуск задач обновления данных погоды в фоновом режиме.
 * Использует {@link ScheduledExecutorService} для периодического выполнения задач.
 */
@Component
@RequiredArgsConstructor
public class WeatherSchedulerComponent {

    private final ScheduledExecutorService scheduledExecutorService;
    private final Map<String, WeatherService> services;
    private final WeatherSchedulerProperties properties;

    /**
     * Инициализирует задачи обновления данных погоды при запуске приложения.
     * <p>
     * Для сервисов, использующих API в режиме POLLING, создаются задачи,
     * которые выполняются с фиксированной задержкой.
     */
    @PostConstruct
    public void init() {
        services.entrySet().stream()
                .filter(a -> a.getValue().getType().equals(TypeApiEnum.POLLING))
                .forEach(a ->
                        scheduledExecutorService
                                .scheduleWithFixedDelay(a.getValue()::updateWeatherData, 1, properties.getTimeDelay(), TimeUnit.MINUTES));
    }
}
