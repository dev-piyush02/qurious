package com.qurious.mappers;

import com.qurious.DTO.UserDTO;
import com.qurious.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper (componentModel = "spring")
public interface UserMapper {
    UserDTO userToUserDTO(User user);
    User userDTOToUser(UserDTO userDTO);

    List<UserDTO> userListToUserDTOList(List<User> userList);
    List<User> userListToUserList(List<User> userList);
}
