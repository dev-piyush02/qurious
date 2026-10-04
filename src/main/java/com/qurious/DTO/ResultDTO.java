package com.qurious.qurious.DTO;

import lombok.Data;

import java.sql.Time;

@Data
public class ResultDTO {
    private int totalCorrectAnswers;
    private Double score;
    private String totalTimeTaken;
}
