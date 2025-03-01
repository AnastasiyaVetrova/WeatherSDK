package com.project.service;

import com.project.dto.WeatherResponse;
import com.project.properties.ApiKey;
import com.project.properties.TypeApiEnum;
import lombok.Getter;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Сервис для получения и кеширования данных о погоде.
 * <p>
 * Использует {@link WebClient} для выполнения HTTP-запросов и {@link WeatherCache} для хранения данных в кеше.
 */
public class WeatherService {
    private final WebClient webClient;
    private final WeatherCache weatherCache;
    private final String lang;
    private final String apiKey;
    private final String urlWeather;
    @Getter
    private final TypeApiEnum type;

    /**
     * Создает экземпляр сервиса погоды.
     *
     * @param webClient    клиент для выполнения HTTP-запросов
     * @param weatherCache кэш для хранения данных о погоде
     * @param apiKey       объект API-ключа, содержащий язык, ключ и тип API
     */
    public WeatherService(WebClient webClient, WeatherCache weatherCache, ApiKey apiKey) {
        this.webClient = webClient;
        this.weatherCache = weatherCache;
        this.lang = apiKey.getLang();
        this.apiKey = apiKey.getApiKey();
        this.type = apiKey.getType();
        this.urlWeather = "?q=%s&lang=%s&appid=%s";
    }

    /**
     * Получает данные о погоде для указанного города.
     * <p>
     * Если данные о городе есть в кеше и они актуальны, они возвращаются из кеша.
     * В противном случае выполняется запрос к API.
     *
     * @param city название города
     * @return {@link Mono} с {@link WeatherResponse}
     */
    public Mono<WeatherResponse> getWeather(String city) {
        return Mono.justOrEmpty(weatherCache.get(city))
                .filter(weatherCache::isCachedValid)
                .switchIfEmpty(Mono.defer(() -> fetchWeather(city)));
    }

    /**
     * Выполняет запрос к API для получения данных о погоде в указанном городе.
     * <p>
     * Результат сохраняется в кеше после успешного ответа.
     *
     * @param city название города
     * @return {@link Mono} с {@link WeatherResponse}
     */
    private Mono<WeatherResponse> fetchWeather(String city) {
        return webClient.get()
                .uri(String.format(urlWeather, city, lang, apiKey))
                .retrieve()
                .bodyToMono(WeatherResponse.class)
                .doOnNext(res -> weatherCache.saveCacheWeather(city, res))
                .onErrorResume(Mono::error);
    }

    /**
     * Обновляет данные о погоде для всех городов, хранящихся в кеше.
     * <p>
     * Выполняет повторные запросы к API и обновляет кеш.
     */
    public void updateWeatherData() {
        weatherCache.getCache().forEach((key, value) -> fetchWeather(key).subscribe());
    }

    /**
     * Очищает кеш с данными о погоде.
     */
    public void clearCache() {
        weatherCache.clear();
    }
}
