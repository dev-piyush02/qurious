package com.qurious.DTO;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class WSHelperDTOs {
    private WSHelperDTOs() {}

    public record SkipRequest(@NotBlank String questionId) {}
    public record QuizFinishedDto(Long quizId, int answered, int skipped, int score) {}
    public record ErrorDto(String code, String message) {}
}
