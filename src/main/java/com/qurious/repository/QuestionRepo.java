package com.qurious.repository;

import com.qurious.entity.Quiz;
import com.qurious.entity.QuizQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface QuestionRepo extends JpaRepository<QuizQuestion,Long> {
    @Query(value = "select * from quiz_questions qq where qq.quiz_id = :quizId order by qq.question_id offset :skpQues limit 1",
            nativeQuery = true)
    Optional<QuizQuestion> findQuestionAtOffset(@Param("quizId") Long quizId,
                                                @Param("skpQues") int skpQues);
}
