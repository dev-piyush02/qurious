package com.qurious.DTO;

import lombok.Data;
import java.sql.Timestamp;

@Data
public class QuizRoomDTO {
    private String roomId;
    private QuizDTO quiz;
    private String roomCode;
    private UserDTO quizRoomHost;
    private String roomStatus;
    private Timestamp createdAt;
}
