package com.blogapplication.application.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.blogapplication.application.Exception.ResourceNotFoundException;
import com.blogapplication.application.Repository.UserRepository;
import com.blogapplication.application.entity.User;


@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

   
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO Auto-generated method stub


       User user= userRepository.findByEmail(username).orElseThrow(()-> new ResourceNotFoundException("User","email :"+username,0));
        
        return user;
    }
    
}
