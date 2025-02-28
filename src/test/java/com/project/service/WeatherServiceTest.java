package com.project.service;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.anyUrl;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.project.dto.Sys;
import com.project.dto.Temperature;
import com.project.dto.Weather;
import com.project.dto.WeatherResponse;
import com.project.dto.Wind;
import com.project.properties.ApiKey;
import com.project.properties.TypeApiEnum;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {

    private final String city = "Moscow";

    @Mock
    private WeatherCache cache;
    @Mock
    private ApiKey apiKey;

    private WeatherService weatherService;

    @RegisterExtension
    static WireMockExtension wireMock = WireMockExtension.newInstance()
            .options(options().dynamicPort()).build();

    @BeforeEach
    void setUp() {
        when(apiKey.getLang()).thenReturn("en");
        when(apiKey.getApiKey()).thenReturn("testKey");
        when(apiKey.getType()).thenReturn(TypeApiEnum.ON_DEMAND);

        WebClient webClient = WebClient.builder().baseUrl(wireMock.baseUrl()).build();

        weatherService = new WeatherService(webClient, cache, apiKey);
    }

    @Test
    void testReturnCached_WeatherIfValid() {
        WeatherResponse cacheResponse = getMonoResponse();

        when(cache.get(city)).thenReturn(cacheResponse);
        when(cache.isCachedValid(cacheResponse)).thenReturn(true);

        StepVerifier.create(weatherService.getWeather(city))
                .expectNext(cacheResponse)
                .verifyComplete();

        verify(cache, times(1)).get(city);
        verify(cache, times(1)).isCachedValid(cacheResponse);
        verify(cache, never()).saveCacheWeather(anyString(), any(WeatherResponse.class));
    }

    @Test
    void testFetchWeather_WhenCacheIsEmpty() {
        WeatherResponse monoRes = getMonoResponse();

        when(cache.get(city)).thenReturn(null);

        setupWireMockSuccessResponse();

        StepVerifier.create(weatherService.getWeather(city))
                .consumeNextWith(res -> {
                    assertEquals(monoRes.getName(), res.getName());
                    assertEquals(monoRes.getVisibility(), res.getVisibility());
                    assertEquals(monoRes.getTemperature().getTemp(), res.getTemperature().getTemp());
                    assertEquals(monoRes.getWeather().getMain(), res.getWeather().getMain());
                })
                .verifyComplete();

        verify(cache, times(1)).get(city);
        verify(cache, times(1)).saveCacheWeather(anyString(), any(WeatherResponse.class));
    }

    @Test
    void testFetchWeather_CacheIsInvalid() {
        WeatherResponse monoRes = getMonoResponse();

        when(cache.get(city)).thenReturn(monoRes);
        when(cache.isCachedValid(monoRes)).thenReturn(false);

        setupWireMockSuccessResponse();

        StepVerifier.create(weatherService.getWeather(city))
                .consumeNextWith(res -> {
                    assertEquals(monoRes.getName(), res.getName());
                    assertEquals(monoRes.getVisibility(), res.getVisibility());
                    assertEquals(monoRes.getTemperature().getTemp(), res.getTemperature().getTemp());
                    assertEquals(monoRes.getWeather().getMain(), res.getWeather().getMain());
                })
                .verifyComplete();

        verify(cache, times(1)).get(city);
        verify(cache, times(1)).isCachedValid(monoRes);
        verify(cache, times(1)).saveCacheWeather(anyString(), any(WeatherResponse.class));
    }

    @Test
    void testUpdateWeatherData_ForAllCachedCities() {
        WeatherResponse response1 = getMonoResponse();
        WeatherResponse response2 = getMonoResponse();

        Map<String, WeatherResponse> cities = new HashMap<>();
        cities.put("Moscow", response1);
        cities.put("London", response2);

        when(cache.getCache()).thenReturn(cities);

        setupWireMockSuccessResponse();
        weatherService.updateWeatherData();

        StepVerifier.create(Mono.delay(Duration.ofSeconds(1)))
                .expectNextCount(1)
                .verifyComplete();

        verify(cache, times(1)).getCache();
        verify(cache, times(2)).saveCacheWeather(anyString(), any(WeatherResponse.class));
    }

    @Test
    void testFetchWeather_WhenErrorResponseApi() {
        when(cache.get(city)).thenReturn(null);

        wireMock.stubFor(get(anyUrl())
                .willReturn(aResponse().withStatus(500)));

        StepVerifier.create(weatherService.getWeather(city))
                .expectError(WebClientResponseException.class)
                .verify();

        verify(cache, times(1)).get(city);
        verify(cache, never()).saveCacheWeather(anyString(), any(WeatherResponse.class));
    }

    private void setupWireMockSuccessResponse() {
        wireMock.stubFor(get(anyUrl())
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(getApiResponse())
                        .withStatus(200)));
    }

    private String getApiResponse() {
        return """
                    {
                      "coord": {
                          "lon": 37.6156,
                          "lat": 55.7522
                      },
                      "weather": [
                          {
                              "id": 800,
                              "main": "Clear",
                              "description": "clear sky",
                              "icon": "01d"
                          }
                      ],
                      "base": "stations",
                      "main": {
                          "temp": 275.39,
                          "feels_like": 275.39,
                          "temp_min": 275.39,
                          "temp_max": 275.44,
                          "pressure": 1027,
                          "humidity": 69,
                          "sea_level": 1027,
                          "grnd_level": 1007
                      },
                      "visibility": 10000,
                      "wind": {
                          "speed": 1.17,
                          "deg": 274,
                          "gust": 1.79
                      },
                      "clouds": {
                          "all": 7
                      },
                      "dt": 1740660278,
                      "sys": {
                          "type": 1,
                          "id": 9027,
                          "country": "RU",
                          "sunrise": 1740630355,
                          "sunset": 1740668337
                      },
                      "timezone": 10800,
                      "id": 524901,
                      "name": "Moscow",
                      "cod": 200
                  }
                """;
    }

    private WeatherResponse getMonoResponse() {

        return new WeatherResponse(
                new Weather("Clear", "clear sky"),
                new Temperature(275.39, 275.39),
                10000,
                new Wind(1.17),
                1740660278,
                new Sys(1740630355, 1740668337),
                10800,
                "Moscow",
                1740660278);
    }
}