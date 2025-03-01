# Проект WeatherSDK

👋 Привет! Меня зовут Анастасия.  
Этот проект предоставляет базовую реализацию для работы с API погоды, включая обработку данных о погоде, кэширование и
обработку исключений. Основной функционал включает извлечение данных о погоде, хранение этих данных в кэше и обновление
информации с использованием периодических запросов.

## Описание

Проект извлекает данные о погоде с использованием API Current Weather Data от OpenWeatherMap. Для работы с этим API
необходимо получить API-ключ, который используется для аутентификации запросов.

С помощью API можно получить текущие данные о погоде в различных городах, а также настроить язык и другие параметры.

## Получение API-ключа
1. Перейдите на сайт OpenWeatherMap.
2. Зарегистрируйтесь и получите свой API-ключ для доступа к данным о погоде.

## Проект включает следующие компоненты:

- **WeatherService**: Сервис для получения данных о погоде через API.
- **WeatherCache**: Кэш для хранения данных о погоде и их обновления.
- **WeatherSchedulerComponent**: Компонент для периодического обновления данных о погоде.
- **ResponseExceptionHandler**: Обработка ошибок для различных исключений при работе с API.
- **WeatherResponse**: Модель данных для ответа от API с информацией о погоде.
- **WeatherServiceManager**: Менеджер сервисов погоды для управления подключенными API-ключами и сервисами.
- **KeyProperties**: Конфигурация для хранения API-ключей.

## Установка

1. Клонируйте репозиторий:

```bash
   https://github.com/AnastasiyaVetrova/WeatherSDK
```

2. Перейдите в каталог проекта:

```bash
   cd "path to folder"
```

3. Соберите и запустите проект с помощью Maven:

```bash
   mvn clean install
```

## Добавление зависимости в Maven

Чтобы подключить библиотеку WeatherSDK к вашему проекту, добавьте следующую зависимость в файл `pom.xml`:

```xml

<dependency>
    <groupId>com.project</groupId>
    <artifactId>WeatherSDK</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

## Конфигурация

Вам нужно будет настроить файл `application.yml` или `application.properties` для указания API-ключей и настроек для
работы с погодным API. Для каждого ключа необходимо выбрать режим работы и язык, на котором будет приходить ответ.
Пример конфигурации:

### Пример application.yml

```yaml
weather:
  apiKeys:
    - apiKey: "your_api_key1"
      type: ON_DEMAND
      lang: "ru"
    - apiKey: "your_api_key2"
      type: POLLING
      lang: "en"

  scheduler:
    threadPool: 5
    timeDelay: 10
```

## Использование

Для использования WeatherSDK в вашем проекте, необходимо подключить зависимость и инжектировать сервис:

```java
private final WeatherServiceManager weatherServices;
```

После этого вы можете использовать методы для работы с погодным сервисом:

### Основные методы:

- `WeatherService getService(String apiKey)` — Получение конкретного сервиса погоды по API-ключу.

```java
WeatherService weatherService = weatherServices.getService("your_api_key");
```

- `Map<String, WeatherService> getAllServices()` — Получение всех доступных сервисов.

```java
Map<String, WeatherService> allServices = weatherServices.getAllServices();
```

- `void removeWeatherService(String apiKey)` — Удаление сервиса погоды по API-ключу.

```java
weatherServices.removeWeatherService("your_api_key");
```

### Получение погоды:

Для получения данных о погоде используйте метод:

```java
Mono<WeatherResponse> getWeather(String city);
```

Этот метод возвращает объект Mono<WeatherResponse>, содержащий данные о погоде в указанном городе.

### Работа с кэшем:

Также предусмотрен метод для очистки кэша:

```java
void clearCache();
```

## Режимы работы

Есть два режима работы погодного сервиса:

- **ON_DEMAND** — Режим для **ручного** вызова данных о погоде. Погода будет загружена только при вызове
  метода `getWeather(String city)`.
- **POLLING** — Режим для **автоматического обновления** данных в кэше через заданный период времени. Перед
  использованием этого режима, необходимо заранее добавить города для отслеживания с помощью
  метода `getWeather(String city)`.

Этот функционал позволяет гибко работать с погодными данными в зависимости от потребностей вашего проекта.