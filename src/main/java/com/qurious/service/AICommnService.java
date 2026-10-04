package com.qurious.qurious.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.qurious.qurious.entity.AIGenLog;
import com.qurious.qurious.utils.JsonHelper;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//this will call model
@Service
public class AICommnService {

    private AIGenLog aiGenLog;
    private final ChatClient chatClient;
    private final JsonHelper jsonHelper;

    public AICommnService(ChatClient.Builder builder, JsonHelper jsonHelper) {
        this.chatClient = builder.build();
        this.jsonHelper = jsonHelper;
    }

    public JsonNode askAI(String prompt) throws JsonProcessingException {
        String resp= chatClient
                .prompt()
                .system("Act as expert question generator." +
                        "Respond only with valid JSON." +
                        "Use no markdown." +
                        "Explanation for answer is required."+
                        "Ensure a valid JSON and parsable through jackson."+
                        "Strictly use this JSON format" +
                        "{\n" +
                        "  \"questions\": [\n" +
                        "    {\n" +
                        "      \"id\": number,\n" +
                        "      \"question\": string,\n" +
                        "      \"options\": [string, string, string, string],\n" +
                        "      \"answer\": string,\n" +
                        "      \"explanation\": string\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}")
                .user(prompt)
                .call()
                .content();
        System.out.println(resp);
        return jsonHelper.toJson(resp);
    }


    public String askStringAI(String prompt) throws JsonProcessingException {
        String resp= chatClient
                .prompt()
                .system("Act as expert question generator." +
                        "Respond only with valid JSON." +
                        "Use no markdown." +
                        "Explanation for answer is required."+
                        "Ensure a valid JSON and parsable through jackson."+
                        "Strictly use this JSON format" +
                        "{\n" +
                        "  \"questions\": [\n" +
                        "    {\n" +
                        "      \"id\": number,\n" +
                        "      \"question\": string,\n" +
                        "      \"options\": [string, string, string, string],\n" +
                        "      \"answer\": string,\n" +
                        "      \"explanation\": string\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}")
                .user(prompt)
                .call()
                .content();
        System.out.println(resp);
        return resp;
    }
}
//EP-> https://api.openai.com/v1/responses

