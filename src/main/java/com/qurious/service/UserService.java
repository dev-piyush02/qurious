package com.qurious.service;

import com.qurious.DTO.UserDTO;
import com.qurious.entity.User;
import com.qurious.enums.Roles;
import com.qurious.mappers.UserMapper;
import com.qurious.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final PasswordEncoder encoder;

    UserService(UserRepo userRepo, UserMapper userMapper, PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.userMapper = userMapper;
        this.encoder= encoder;
    }

    public List<User> findAllUser(){
        return userRepo.findAll();
    }

    public UserDTO findUserById(String id){
        User user= userRepo.findById(id).get();
        return userMapper.userToUserDTO(user);
    }

    public Boolean addUser(User user){
        if(!userRepo.existsById(user.getUserId())){
            user.setUserPassword(encoder.encode(user.getUserPassword()));
            user.setUserRole(Roles.USER);
            userRepo.save(user);
            return true;
        }
        return false;
    }

    public Boolean updateUser(User newUser){
        User userInDB = userRepo.findById(newUser.getUserId()).get();
        if(userInDB.getUserId().equals(newUser.getUserId())){
            userInDB.setUserEmail(newUser.getUserEmail());
            userInDB.setUserName(newUser.getUserName());
            userRepo.save(userInDB);
            return true;
        }
        return false;
    }

    public Boolean deleteUser(String id){
        User userInDB = userRepo.findById(id).get();
        if(userInDB.getUserId().equals(id)){
            userRepo.delete(userInDB);
            return true;
        }
        return false;
    }

    public User loadUserById(String id){
        return userRepo.findById(id).get();
    }
}
