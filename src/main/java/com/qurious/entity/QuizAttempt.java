package com.qurious.entity;

import com.qurious.enums.QuizAttemptStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
@Table(name="quiz_attempt", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "room_id"})})
public class QuizAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attemptId;
    //FK to user.userId
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name= "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name="quiz_attempt_fkey")
    )
    private User user;
    //FK to quiz.quizId
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "quiz_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fkey_quiz_tbl")
    )
    private Quiz quiz;
    //FK to quizRoom.roomId
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "room_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fkey_quiz_room_tbl")
    )
    private QuizRoom quizRoom;
    private Double score;
    private QuizAttemptStatus status;
    private int seenQuesCnt;
    private Timestamp startedAt;
    private Timestamp submittedAt;
}
