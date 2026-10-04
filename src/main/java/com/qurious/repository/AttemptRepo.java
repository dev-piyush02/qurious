package com.qurious.qurious.repository;

import com.qurious.qurious.entity.QuizAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttemptRepo extends JpaRepository<QuizAttempt, Long> {
}
