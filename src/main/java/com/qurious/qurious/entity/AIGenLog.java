package com.qurious.qurious.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
@Table(name="ai_quiz_gen_log")
public class AIGenLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long genLogId;
    //FK to quiz.quizId
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name="quiz_id",
            nullable = false,
            foreignKey = @ForeignKey(name="fkey_quiz_tbl")
    )
    private Quiz quiz;
    private String topic;
    private String difficulty;
    private int numOfQuestions;
    private String aiModel;
    private String promptText;
    private Timestamp createdAt;
}
