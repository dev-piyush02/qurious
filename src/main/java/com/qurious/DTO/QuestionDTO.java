package com.qurious.qurious.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class QuestionDTO {

    private Long questionId;
    private String questionText;
    private String questionType;
    private String correctAnswer;
    private String answerExplanation;
    private List<AnswerOptionDTO> options = new ArrayList<>();
}
