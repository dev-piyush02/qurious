package com.qurious.qurious.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qurious.qurious.enums.Roles;
import com.qurious.qurious.enums.Roles;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name= "users")
public class User {
    @Id
    @JsonProperty("unique-id")
    @Column(nullable = false, unique = true)
    private String userId;

    @JsonProperty("user-name")
    private String userName;

    @JsonProperty("user-email")
    @Column(nullable = false, unique = true)
    private String userEmail;

    @JsonProperty("password")
    @Column(nullable = false)
    private String userPassword;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Roles userRole;
}
