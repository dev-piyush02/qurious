package com.qurious.DTO;

import lombok.Data;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
public class QuizDTO {
    private Long quizId;
    private String quizTitle;
    private String quizDescription;
    private String quizDifficulty;
    private int totalQuestions;
    private UserDTO createdBy;
    private Timestamp createdOn;
    private Timestamp updatedOn;
    private String status;
    private Timestamp creationTime;
    private int timeDuration;
    private List<QuestionDTO> questions = new ArrayList<>();
}
