package com.qurious.qurious.service;

import com.qurious.qurious.entity.User;
import com.qurious.qurious.repository.UserRepo;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepo userRepo;
    UserDetailServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }


    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user= userRepo.findById(userId).get();
        if(user.getUserEmail()==null)
            throw new UsernameNotFoundException(userId);
        return org.springframework.security.core.userdetails.User.builder()
                .username(userId)
                .password(user.getUserPassword())
                .roles(user.getUserRole().toString())
                .build();
    }
}
