package org.eng_diary.api.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class JsonBuilder {
    private final ObjectMapper objectMapper;
    private final ObjectNode rootNode;

    public JsonBuilder() {
        this.objectMapper = new ObjectMapper();
        this.rootNode = objectMapper.createObjectNode();
    }

    public JsonBuilder addField(String key, String value) {
        rootNode.put(key, value);
        return this;
    }

    public JsonBuilder addField(String key, int value) {
        rootNode.put(key, value);
        return this;
    }

    public JsonBuilder addField(String key, boolean value) {
        rootNode.put(key, value);
        return this;
    }

    public JsonBuilder addObject(String key, ObjectNode objectNode) {
        rootNode.set(key, objectNode);
        return this;
    }

    public JsonBuilder addArray(String key, ArrayNode arrayNode) {
        rootNode.set(key, arrayNode);
        return this;
    }

    public ObjectNode build() {
        return rootNode;
    }

    public String buildAsString() {
        try {
            return objectMapper.writeValueAsString(rootNode);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 파싱 오류", e);
        }
    }
}
