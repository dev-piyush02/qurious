package com.qurious.qurious.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name= "quiz")
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long quizId;
    private String quizTitle;
    private String quizDescription;
    private String quizDifficulty;
    private int totalQuestions;
    private String status;
    private Timestamp creationTime;
    private int timeDuration;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name= "created_by",
            nullable = false,
            foreignKey = @ForeignKey(name = "fkey_user_tbl")
    )
    private User createdBy;
    @OneToMany(mappedBy = "quiz",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JsonManagedReference
    private List<QuizQuestion> questions = new ArrayList<>();
}
