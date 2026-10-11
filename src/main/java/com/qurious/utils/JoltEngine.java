package com.qurious.utils;

import com.bazaarvoice.jolt.Chainr;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Component
public class JoltEngine {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Object transform(String json, String specPath)
            throws Exception {

        Map<String, Object> input =
                objectMapper.readValue(json, Map.class);

        InputStream specStream =
                getClass().getResourceAsStream(specPath);

        List<Object> spec =
                objectMapper.readValue(specStream, List.class);

        Chainr chainr = Chainr.fromSpec(spec);

        return chainr.transform(input);
    }
}

