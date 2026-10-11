package com.qurious.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class QuizRoomReqDTO {
    @JsonProperty("room-code")
    private String roomCode;
    @JsonProperty("room-id")
    private String roomId;
}
