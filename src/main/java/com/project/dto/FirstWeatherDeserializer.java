package com.project.dto;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

/**
 * Десериализатор для поля {@link Weather}, предназначенный для обработки ответа API погоды.
 * <p>
 * Этот десериализатор ожидает, что ответ от API будет содержать массив с погодными данными,
 * и извлекает первый элемент этого массива для преобразования в объект {@link Weather}.
 */
public class FirstWeatherDeserializer extends JsonDeserializer<Weather> {
    /**
     * Десериализует JSON-данные в объект {@link Weather}.
     * <p>
     * Если JSON-ответ представляет собой массив, то извлекается первый элемент и преобразуется в объект {@link Weather}.
     * Если массив пуст или ответ не является массивом, возвращается {@code null}.
     *
     * @param jsonParser             Парсер, который используется для чтения JSON.
     * @param deserializationContext Контекст десериализации.
     * @return Объект {@link Weather}, полученный из первого элемента массива.
     * @throws IOException      Если произошла ошибка при чтении данных.
     * @throws JacksonException Если произошла ошибка при обработке JSON.
     */
    @Override
    public Weather deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        if (node.isArray() && !node.isEmpty()) {
            return jsonParser.getCodec().treeToValue(node.get(0), Weather.class);
        }
        return null;
    }
}
