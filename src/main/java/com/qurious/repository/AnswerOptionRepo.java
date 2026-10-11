package com.qurious.repository;

import com.qurious.entity.AnswerOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerOptionRepo extends JpaRepository<AnswerOption,Long> {
    List<AnswerOption> findAllByQuizQuestion_questionId(Long quesId);
}
