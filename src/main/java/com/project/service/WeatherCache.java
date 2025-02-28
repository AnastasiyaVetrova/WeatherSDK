package com.project.service;

import com.project.dto.WeatherResponse;
import lombok.Getter;

import java.time.Instant;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class WeatherCache {
    private final Map<String, WeatherResponse> cache = new ConcurrentHashMap<>();
    private static final int CACHE_SIZE = 10;
    private static final int CACHE_TIME = 600;

    public boolean isCachedValid(WeatherResponse weatherResponse) {
        return Instant.now().getEpochSecond() - weatherResponse.getTimeNow() < CACHE_TIME;
    }

    public void saveCacheWeather(String city, WeatherResponse weatherResponse) {
        if (cache.size() >= CACHE_SIZE) {
            removeOldCache();
        }
        cache.put(city, weatherResponse);
    }

    public void removeOldCache() {
        cache.entrySet()
                .stream()
                .min(Comparator.comparing(a -> a.getValue().getTimeNow()))
                .ifPresent(a -> cache.remove(a.getKey()));
    }

    public WeatherResponse get(String city) {
        return cache.get(city);
    }

    public void clear() {
        cache.clear();
    }
}
