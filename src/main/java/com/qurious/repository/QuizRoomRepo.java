package com.qurious.qurious.repository;

import com.qurious.qurious.entity.QuizRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizRoomRepo extends JpaRepository <QuizRoom, String>{
    public QuizRoom findQuizRoomByRoomCode(String roomCode);

    public List<QuizRoom> findQuizRoomByRoomStatus(String status);

    public List<QuizRoom> findAllQuizRoomByQuizRoomHost_UserId(String userId);
}
