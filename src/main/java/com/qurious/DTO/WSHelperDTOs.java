package com.qurious.DTO;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class WSHelperDTOs {
    private WSHelperDTOs() {}

    public record AnswerRequest(@NotBlank String questionId, @NotBlank String optionId) {}
    public record SkipRequest(@NotBlank String questionId) {}
    public record QuizFinishedDto(String quizId, int answered, int skipped) {}
    public record ErrorDto(String code, String message) {}
}
