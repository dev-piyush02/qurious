package com.qurious.repository;

import com.qurious.entity.AnswerSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerSubmissionRepo extends JpaRepository<AnswerSubmission, Long> {
    Integer countByParticipant_UserIdAndQuizRoom_RoomIdAndIsCorrectIsTrue(String userId, String roomId);
}
