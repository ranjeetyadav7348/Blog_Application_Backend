package com.blogapplication.application.Service;

import java.util.List;

import com.blogapplication.application.entity.User;
import com.blogapplication.application.payloads.UserDto;

public interface UserService {

    UserDto createUser(UserDto user);
    UserDto updateUser(UserDto user,Integer userId);
    UserDto getUserById(Integer userId);
    List<UserDto> getAllUser();
    void deleteUser(Integer userId);
    
    


    
} 