package com.qurious.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name="answer_option")
public class AnswerOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long optId;
    //FK to QuizQuestion.questionId
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name="question_id",
            nullable = false,
            foreignKey = @ForeignKey(name="fkey_quiz_question_tbl")
    )
    @JsonBackReference
    @ToString.Exclude
    private QuizQuestion quizQuestion;
    private String optText;
    private boolean isCorrect;
}
