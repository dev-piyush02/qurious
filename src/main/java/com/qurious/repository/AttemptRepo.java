package com.qurious.repository;

import com.qurious.entity.QuizAttempt;
import com.qurious.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AttemptRepo extends JpaRepository<QuizAttempt, Long> {
    QuizAttempt findByUser_UserIdAndQuiz_QuizId(String userId, Long quizId);
    @Query("select a.seenQuesCnt from QuizAttempt a where a.user.userId = :userId and a.quiz.quizId = :quizId")
    Integer findSeenQuesCnt(@Param("userId") String userId, @Param("quizId") Long quizId);

    @Modifying
    @Query("update QuizAttempt qa set qa.seenQuesCnt= :seenQuesCnt")
    void incrementCnt(@Param("seenQuesCnt") int seenQuesCnt);
}
