package com.qurious.qurious.repository;

import com.qurious.qurious.entity.QuizQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepo extends JpaRepository<QuizQuestion,Long> {

}
