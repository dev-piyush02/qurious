package com.qurious.qurious.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserDTO {
    @JsonProperty("user-id")
    private String userId;
    @JsonProperty("user-name")
    private String userName;
    @JsonProperty("user-email")
    private String userEmail;
}
