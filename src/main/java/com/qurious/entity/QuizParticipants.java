package com.qurious.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
@Table(name= "quiz_participants", uniqueConstraints = {@UniqueConstraint(columnNames = {"participant_id", "room_id"})})
public class QuizParticipants {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // FK to user.userId
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name= "participant_id",
            nullable = false,
            foreignKey = @ForeignKey(name="fkey_user_tbl")
    )
    private User participant;
    //FK to quizRoom.roomId
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name="room_id",
            nullable = false,
            foreignKey = @ForeignKey(name="fkey_quiz_room_tbl")
    )
    private QuizRoom quizRoom;
    private Timestamp joinedAt;
    private boolean isCompleted;
}
