package com.qurious.repository;

import com.qurious.entity.QuizParticipants;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepo extends JpaRepository<QuizParticipants,Long> {

}
