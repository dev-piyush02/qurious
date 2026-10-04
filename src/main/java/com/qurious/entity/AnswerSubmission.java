package com.qurious.qurious.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Time;
import java.sql.Timestamp;

@Data
@Entity
@Table(name="answer_submission", uniqueConstraints ={@UniqueConstraint(columnNames = {"room_id", "question_id", "participant_id"})})
public class AnswerSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //FK to quizRoom.roomId
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name="room_id",
            nullable = false,
            foreignKey = @ForeignKey(name="fkey_quiz_room_tbl")
    )
    @JsonIgnore
    private QuizRoom quizRoom;
    //FK to QuizQuestion.questionId
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name="question_id",
            nullable = false,
            foreignKey = @ForeignKey(name="fkey_quiz_question_tbl")
    )
    @JsonIgnore
    private QuizQuestion quizQuestion;
    // FK to user.userId
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name= "participant_id",
            nullable = false,
            foreignKey = @ForeignKey(name="quiz_participants_fkey")
    )
    @JsonIgnore
    private User participant;
    private String selectedAnswer;
    private boolean isCorrect;
    private Timestamp submittedAt;
    private Long responseTimeMS;
}
