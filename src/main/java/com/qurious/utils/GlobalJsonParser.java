package com.qurious.qurious.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class GlobalJsonParser {

    private final JoltEngine joltEngine;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public GlobalJsonParser(JoltEngine joltEngine) {
        this.joltEngine = joltEngine;
    }

    public <T> T parse(String json,
                       String specPath,
                       TypeReference<T> typeReference) throws Exception {

        // Step 1: Transform JSON using JOLT
        Object transformed =
                joltEngine.transform(json, specPath);

        // Step 2: Bind to Java object
        return objectMapper.convertValue(transformed, typeReference);
    }
}

