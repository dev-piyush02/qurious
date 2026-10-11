package com.qurious.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NonNull;

@Data
@Entity
@Table(name="scores", uniqueConstraints = {@UniqueConstraint(columnNames = {"room_id", "participant_id"})})
public class Scores {
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
    private QuizRoom quizRoom;
    // FK to user.userId
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name= "participant_id",
            nullable = false,
            foreignKey = @ForeignKey(name="fkey_user_tbl")
    )
    private User participant;
    @Column(nullable = false)
    private Double totalScore;
    private int rank;
}
