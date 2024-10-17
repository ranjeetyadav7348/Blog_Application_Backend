package com.blogapplication.application.Service;

import java.util.List;

import com.blogapplication.application.entity.User;
import com.blogapplication.application.payloads.JwtAuthRequest;
import com.blogapplication.application.payloads.UserDto;

public interface UserService {

    UserDto registerNewUser(UserDto user);
    UserDto createUser(UserDto user);
    UserDto updateUser(UserDto user,Integer userId);
    UserDto getUserById(Integer userId);
    List<UserDto> getAllUser();
    void deleteUser(Integer userId);
    void updatePassword(JwtAuthRequest jwtAuthRequest);
    
    


    
} 