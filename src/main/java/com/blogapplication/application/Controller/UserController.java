package com.blogapplication.application.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blogapplication.application.Service.UserService;
import com.blogapplication.application.payloads.UserDto;
import com.blogapplication.application.payloads.apiResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
@EnableMethodSecurity
public class UserController {


    @Autowired
    private UserService userService;

    @PostMapping("/")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto)
    {
        UserDto createUserDto = userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createUserDto);
      
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto,@PathVariable Integer userId)
    {
        UserDto updatedUserDto = userService.updateUser(userDto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedUserDto);
      
    }




    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<apiResponse> deleteUser(@PathVariable Integer userId)
    {
        userService.deleteUser(userId);
        return new ResponseEntity(new apiResponse("User Deleted Successfully",true),HttpStatus.OK);
        
      
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getSingleUser(@PathVariable Integer userId)
    {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @GetMapping("/")
    public ResponseEntity<List<UserDto>> getUsers()
    {
        return ResponseEntity.ok(userService.getAllUser());
    }


    
}
