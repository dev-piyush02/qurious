package com.qurious.qurious.DTO;

import com.qurious.qurious.entity.QuizQuestion;
import lombok.Data;
import lombok.ToString;

@Data
public class AnswerOptionDTO {
    private Long optId;
//    @ToString.Exclude
//    private QuestionDTO quizQuestion;
    private String optText;
    private boolean isCorrect;
}
