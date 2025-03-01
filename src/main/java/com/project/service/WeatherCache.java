package com.project.service;

import com.project.dto.WeatherResponse;
import lombok.Getter;

import java.time.Instant;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Класс для кеширования данных о погоде.
 * <p>
 * Хранит ограниченное количество записей в кеше и автоматически удаляет устаревшие данные.
 */
@Getter
public class WeatherCache {
    private final Map<String, WeatherResponse> cache = new ConcurrentHashMap<>();
    private static final int CACHE_SIZE = 10;
    private static final int CACHE_TIME = 600;

    /**
     * Проверяет, является ли кешированное значение актуальным.
     *
     * @param weatherResponse объект с погодными данными
     * @return {@code true}, если данные актуальны, иначе {@code false}
     */
    public boolean isCachedValid(WeatherResponse weatherResponse) {
        return Instant.now().getEpochSecond() - weatherResponse.getTimeNow() < CACHE_TIME;
    }

    /**
     * Сохраняет данные о погоде в кеше.
     * <p>
     * Если кеш достиг максимального размера, удаляется самая старая запись.
     *
     * @param city            название города
     * @param weatherResponse объект с данными о погоде
     */
    public void saveCacheWeather(String city, WeatherResponse weatherResponse) {
        if (cache.size() >= CACHE_SIZE) {
            removeOldCache();
        }
        cache.put(city, weatherResponse);
    }

    /**
     * Удаляет устаревшую запись из кеша.
     * <p>
     * Удаляется запись с самым старым временем обновления.
     */
    public void removeOldCache() {
        cache.entrySet()
                .stream()
                .min(Comparator.comparing(a -> a.getValue().getTimeNow()))
                .ifPresent(a -> cache.remove(a.getKey()));
    }

    /**
     * Возвращает закешированные данные о погоде для указанного города.
     *
     * @param city название города
     * @return {@link WeatherResponse}, если данные найдены, иначе {@code null}
     */
    public WeatherResponse get(String city) {
        return cache.get(city);
    }

    /**
     * Очищает весь кеш.
     */
    public void clear() {
        cache.clear();
    }
}
