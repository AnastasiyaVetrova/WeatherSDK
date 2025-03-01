package com.project;

import com.project.exception.WeatherServiceInitializationException;
import com.project.exception.WeatherServiceNotFoundException;
import com.project.properties.ApiKey;
import com.project.properties.KeyProperties;
import com.project.service.WeatherService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class WeatherServiceConfigurationTest {
    @Mock
    private KeyProperties properties;

    @Mock
    private WebClient webClient;

    private WeatherServiceConfiguration configuration;

    @BeforeEach
    void setUp() {
        ApiKey key1 = new ApiKey();
        key1.setApiKey("key1");

        ApiKey key2 = new ApiKey();
        key2.setApiKey("key2");

        Set<ApiKey> apiKeys = Set.of(key1, key2);
        when(properties.getApiKeys()).thenReturn(apiKeys);

        configuration = new WeatherServiceConfiguration(properties, webClient);
        configuration.weatherServiceAndCacheConfiguration();
    }

    @Test
    void testWeatherCacheConfiguration_CreatesWeatherServices() {
        Map<String, WeatherService> services = configuration.weatherServiceAndCacheConfiguration();

        assertThat(services).hasSize(2);
        assertThat(services).containsKeys("key1", "key2");
    }

    @Test
    void testGetAllServices_ReturnsServices() {
        Map<String, WeatherService> services = configuration.getAllServices();

        assertThat(services).hasSize(2);
        assertThat(services).containsKeys("key1", "key2");
    }

    @Test
    void testGetAllServices_ThrowsExceptionWhenEmpty() {
        WeatherServiceConfiguration emptyConfiguration = new WeatherServiceConfiguration(properties, webClient);

        assertThatThrownBy(emptyConfiguration::getAllServices)
                .isInstanceOf(WeatherServiceInitializationException.class)
                .hasMessage("No WeatherServices were initialized.");
    }

    @Test
    void testGetService_ReturnsCorrectService() {
        WeatherService service = configuration.getService("key1");

        assertThat(service).isNotNull();
    }

    @Test
    void testGetService_ThrowsExceptionForInvalidKey() {
        assertThatThrownBy(() -> configuration.getService("invalid-key"))
                .isInstanceOf(WeatherServiceNotFoundException.class)
                .hasMessage("WeatherService not found for API key: invalid-key");
    }

    @Test
    void testRemoveWeatherService_RemovesService() {
        Map<String, WeatherService> services = configuration.weatherServiceAndCacheConfiguration();
        configuration.removeWeatherService("key1");

        assertThat(services).hasSize(1);
        assertThat(services).doesNotContainKeys("key1");
    }
}