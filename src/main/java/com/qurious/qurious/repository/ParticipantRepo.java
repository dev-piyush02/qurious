package com.qurious.qurious.repository;

import com.qurious.qurious.entity.QuizParticipants;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepo extends JpaRepository<QuizParticipants,Long> {
}
