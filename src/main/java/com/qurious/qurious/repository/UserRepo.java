package com.qurious.qurious.repository;

import com.qurious.qurious.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,String> {
}

