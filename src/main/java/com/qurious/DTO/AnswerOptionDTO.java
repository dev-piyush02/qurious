package com.qurious.DTO;

import com.qurious.entity.QuizQuestion;
import lombok.Data;
import lombok.ToString;

@Data
public class AnswerOptionDTO {
    private Long optId;
    private String optText;
    private boolean isCorrect;
}
