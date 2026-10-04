package com.qurious.qurious.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name= "quiz_questions")
public class QuizQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;
    // FK to quiz.quizId
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "quiz_id",
            nullable = false,
            foreignKey = @ForeignKey(name="fkey_quiz_tbl")
    )
    @JsonBackReference
    @ToString.Exclude
    private Quiz quiz;
    private String questionText;
    private String questionType;
    private String correctAnswer;
    private String answerExplanation;
    @OneToMany(mappedBy = "quizQuestion",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JsonManagedReference
    private List<AnswerOption> options = new ArrayList<>();
}