package com.the.mild.project.server.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class JacksonHandler {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static String stringify(Object jackson) {
        String result = "{}";
        try {
            result = mapper.writeValueAsString(jackson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }

    private JacksonHandler() {
        // Utility
    }
}
