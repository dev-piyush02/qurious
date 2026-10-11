package com.qurious.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserLogin {

    @JsonProperty("user-id")
    private String userId;
    private String password;
}
