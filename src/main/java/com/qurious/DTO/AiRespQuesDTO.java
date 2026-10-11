package com.qurious.DTO;

import lombok.Data;

import java.util.List;

@Data
public class AiRespQuesDTO {

    private long id;
    private String question;
    private List<String> options;
    private String answer;
    private String explanation;

}
