package com.project.service;

import com.project.dto.WeatherResponse;
import lombok.Getter;

import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class WeatherCache {
    private final Map<String, WeatherResponse> cache = new ConcurrentHashMap<>();
    private static final int CACHE_SIZE = 10;
    private static final int CACHE_TIME = 600;

    public boolean isCachedValid(WeatherResponse weatherResponse) {
        return System.currentTimeMillis() / 1000 - weatherResponse.getDatetime() < CACHE_TIME;
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
                .min(Comparator.comparing(a -> a.getValue().getDatetime()))
                .ifPresent(a -> cache.remove(a.getKey()));
    }

    public WeatherResponse get(String city) {
        return cache.get(city);
    }

    public void clear() {
        cache.clear();
    }
}
