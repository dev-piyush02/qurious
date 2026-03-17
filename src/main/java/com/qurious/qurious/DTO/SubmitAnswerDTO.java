package com.qurious.qurious.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SubmitAnswerDTO {
    @JsonProperty("quiz-id")
    private Long quizId;
    @JsonProperty("attempt-id")
    private Long attemptId;
    @JsonProperty("question-id")
    private Long questionId;
    @JsonProperty("answer-id")
    private Long answerId;
}
