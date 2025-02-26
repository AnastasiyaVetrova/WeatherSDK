package com.project.service;

import com.project.dto.WeatherResponse;
import com.project.properties.ApiKey;
import com.project.properties.TypeApiEnum;
import lombok.Getter;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class WeatherService {
    private final WebClient webClient;
    private final WeatherCache weatherCache;
    private final String lang;
    private final String apiKey;
    private final String urlWeather;
    @Getter
    private final TypeApiEnum type;

    public WeatherService(WeatherCache weatherCache, ApiKey apiKey) {
        webClient = WebClient.create();
        this.weatherCache = weatherCache;
        this.lang = apiKey.getLang();
        this.apiKey = apiKey.getApiKey();
        this.type = apiKey.getType();
        this.urlWeather = "https://api.openweathermap.org/data/2.5/weather?q=%s&lang=%s&appid=%s";
    }

    public Mono<WeatherResponse> getWeather(String city) {
        return Mono.justOrEmpty(weatherCache.get(city))
                .filter(weatherCache::isCachedValid)
                .switchIfEmpty(fetchWeather(city));
    }

    private Mono<WeatherResponse> fetchWeather(String city) {
        return webClient.get()
                .uri(String.format(urlWeather, city, lang, apiKey))
                .retrieve()
                .bodyToMono(WeatherResponse.class)
                .doOnNext(res -> weatherCache.saveCacheWeather(city, res))
                .onErrorResume(Mono::error);
    }

    public void updateWeatherData() {
        System.out.println("Начало работы шедулера");
        weatherCache.getCache().forEach((key, value) -> fetchWeather(key).subscribe());
    }

    public void clearCache() {
        weatherCache.clear();
    }
}
