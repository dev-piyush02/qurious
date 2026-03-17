package com.qurious.qurious.repository;

import com.qurious.qurious.entity.QuizParticipants;
import com.qurious.qurious.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizParticipantRepo extends JpaRepository<QuizParticipants, Long> {
    QuizParticipants findByParticipant_UserIdAndQuizRoom_RoomId(String userId, String roomId);
}
