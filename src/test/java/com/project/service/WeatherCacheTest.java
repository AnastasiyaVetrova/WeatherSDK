package com.project.service;

import com.project.dto.WeatherResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

class WeatherCacheTest {

    private WeatherCache cache;

    @BeforeEach
    void setUp() {
        cache = new WeatherCache();
    }

    @Test
    void testIsCachedValid() {
        WeatherResponse validResponse = getWeatherResponse(Instant.now().getEpochSecond());
        WeatherResponse invalidResponse = getWeatherResponse(Instant.now().getEpochSecond() - 700);

        assertTrue(cache.isCachedValid(validResponse));
        assertFalse(cache.isCachedValid(invalidResponse));
    }

    @Test
    void testSaveCacheWeather() {

        cache.saveCacheWeather("Moscow", getWeatherResponse(Instant.now().getEpochSecond()));

        assertEquals(1, cache.getCache().size());
        assertTrue(cache.getCache().containsKey("Moscow"));
    }

    @Test
    void testSaveCacheWeather_WhenSizeCacheLimit() {
        for (int i = 0; i <= 13; i++) {
            cache.saveCacheWeather("Moscow" + i,
                    getWeatherResponse(Instant.now().getEpochSecond() - i));
        }
        assertEquals(10, cache.getCache().size());
    }

    @Test
    void removeOldCache() {
        cache.saveCacheWeather("Moscow", getWeatherResponse(Instant.now().getEpochSecond()));
        cache.saveCacheWeather("London", getWeatherResponse(Instant.now().getEpochSecond() - 500));

        cache.removeOldCache();

        assertTrue(cache.getCache().containsKey("Moscow"));
        assertFalse(cache.getCache().containsKey("London"));

    }

    private WeatherResponse getWeatherResponse(long time) {
        WeatherResponse response = new WeatherResponse();
        response.setTimeNow(time);
        return response;
    }
}