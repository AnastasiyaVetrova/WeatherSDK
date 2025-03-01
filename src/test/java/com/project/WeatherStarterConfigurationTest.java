package com.project;

import com.project.properties.WeatherSchedulerProperties;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.ScheduledExecutorService;

@ExtendWith(MockitoExtension.class)
class WeatherStarterConfigurationTest {
    @Mock
    private WeatherSchedulerProperties properties;

    private WeatherStarterConfiguration schedulerConfiguration;

    @BeforeEach
    void setUp() {
        schedulerConfiguration = new WeatherStarterConfiguration(properties);
    }

    @Test
    void testScheduledExecutorService() {
       when(properties.getThreadPool()).thenReturn(5);

        ScheduledExecutorService scheduled = schedulerConfiguration.scheduledExecutorService();

        assertThat(scheduled).isNotNull();
        assertThat(scheduled).isInstanceOf(ScheduledExecutorService.class);
    }
}