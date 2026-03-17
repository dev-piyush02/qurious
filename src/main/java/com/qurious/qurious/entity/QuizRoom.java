package com.qurious.qurious.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@Entity
@Table(name="quiz_room", uniqueConstraints = {@UniqueConstraint(columnNames = "room_code")})
public class QuizRoom {
    @Id
    private String roomId;
    @PrePersist
    public void generateRoomId() {
        this.roomId = "QUIZ_" + UUID.randomUUID().toString().substring(0, 8);
    }
    // FK to quiz.quizId
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "quiz_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fkey_quiz_tbl")
    )
    private Quiz quiz;
    private String roomCode;
    //FK to user.userId
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name= "room_host",
            nullable = false,
            foreignKey = @ForeignKey(name="fkey_user_tbl")
    )
    private User quizRoomHost;
    private String roomStatus;
    private Timestamp createdAt;
}
