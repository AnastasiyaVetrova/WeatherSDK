package com.project.dto;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class FirstWeatherDeserializer extends JsonDeserializer<Weather> {

    @Override
    public Weather deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        if (node.isArray() && !node.isEmpty()) {
            return jsonParser.getCodec().treeToValue(node.get(0), Weather.class);
        }
        return null;
    }
}
