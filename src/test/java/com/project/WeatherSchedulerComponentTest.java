package com.project;

import com.project.properties.WeatherSchedulerProperties;
import com.project.properties.TypeApiEnum;
import com.project.service.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@ExtendWith(MockitoExtension.class)
class WeatherSchedulerComponentTest {
    @Mock
    private ScheduledExecutorService scheduled;
    @Mock
    private WeatherSchedulerProperties properties;
    @Mock
    private WeatherService pollingService;
    @Mock
    private WeatherService noPollingService;

    private Map<String, WeatherService> services;
    private WeatherSchedulerComponent schedulerComponent;

    @BeforeEach
    void setUp() {
        when(pollingService.getType()).thenReturn(TypeApiEnum.POLLING);
        when(noPollingService.getType()).thenReturn(TypeApiEnum.ON_DEMAND);
        when(properties.getTimeDelay()).thenReturn(10);

        services = new HashMap<>();
        services.put("polling", pollingService);
        services.put("noPolling", noPollingService);

        schedulerComponent = new WeatherSchedulerComponent(scheduled, services, properties);
    }

    @Test
    void testInit_SchedulesPollingService() {
        schedulerComponent.init();

        verify(scheduled, times(1))
                .scheduleWithFixedDelay(any(Runnable.class), eq(1L), eq(10L), eq(TimeUnit.MINUTES));
        verify(pollingService, never()).updateWeatherData();
    }

    @Test
    void testInit_DoesNotScheduleNonPollingService() {
        schedulerComponent.init();

        verify(scheduled, never())
                .scheduleWithFixedDelay(eq(noPollingService::updateWeatherData), eq(1L), eq(10L), eq(TimeUnit.MINUTES));
    }
}