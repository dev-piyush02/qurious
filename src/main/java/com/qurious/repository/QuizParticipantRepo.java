package com.qurious.repository;

import com.qurious.entity.QuizParticipants;
import com.qurious.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizParticipantRepo extends JpaRepository<QuizParticipants, Long> {
    QuizParticipants findByParticipant_UserIdAndQuizRoom_RoomId(String userId, String roomId);
}
