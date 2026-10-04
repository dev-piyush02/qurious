package com.qurious.qurious.repository;

import com.qurious.qurious.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizRepo extends JpaRepository<Quiz,Long> {
//    List<Quiz> findAllByCreatedBy(String createdBy);
    List<Quiz> findAllByCreatedBy_UserId(String userId);

    List<Quiz> findAllByStatus(String status);
}
