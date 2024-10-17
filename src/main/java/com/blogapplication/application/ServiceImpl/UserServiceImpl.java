package com.blogapplication.application.ServiceImpl;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.blogapplication.application.Exception.ResourceNotFoundException;
import com.blogapplication.application.Repository.RoleRepo;
import com.blogapplication.application.Repository.UserRepository;
import com.blogapplication.application.Service.UserService;
import com.blogapplication.application.entity.Role;
import com.blogapplication.application.entity.User;
import com.blogapplication.application.payloads.JwtAuthRequest;
import com.blogapplication.application.payloads.UserDto;


@Component
public class UserServiceImpl implements UserService {


    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired 
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepo roleRepo;

 

    @Override
    public UserDto createUser(UserDto userDto) {

        User user=this.dtoToUser(userDto);

        User savedUser=this.userRepo.save(user);
        return this.userToDto(savedUser);
    }

    @Override
    public void deleteUser(Integer userId) {

        User user=userRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User","id",userId));
       
        userRepo.delete(user);
        
    }

    @Override
    public List<UserDto> getAllUser() {

    List<User>users=userRepo.findAll();
    List<UserDto> userDtos=users.stream().map(user->userToDto(user)).collect(Collectors.toList());

     

        return userDtos;
    }



    @Override
    public UserDto getUserById(Integer userId) {

        User user=userRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User","id",userId));
       
        return userToDto(user);
    }

    @Override
    public UserDto updateUser(UserDto userDto, Integer userId) {
       

        User user=userRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User","id",userId));
         
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setAbout(userDto.getAbout());

        User updatedUser=userRepo.save(user);

        UserDto userDto1= userToDto(updatedUser);


        return userDto1;
    }


    private User dtoToUser(UserDto userDto)
    {
        User user=modelMapper.map(userDto,User.class);
        // user.setId(userDto.getId());
        // user.setName(userDto.getName());
        // user.setEmail(userDto.getEmail());
        // user.setAbout(userDto.getAbout());
        // user.setPassword(userDto.getPassword());
        return user;
    }

    
    private UserDto userToDto(User user)
    {
        UserDto userDto=modelMapper.map(user,UserDto.class);
        // userDto.setId( user.getId());
        // userDto.setName( user.getName());
        // userDto.setEmail( user.getEmail());
        // userDto.setAbout( user.getAbout());
        // userDto.setPassword( user.getPassword());
        return userDto;
    }

    @Override
    public UserDto registerNewUser(UserDto userDto) {
        // TODO Auto-generated method stub

        User user= modelMapper.map(userDto,User.class);

        //encode the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        

        Role role=roleRepo.findById(1).get();
        user.getRoles().add(role);
        User newUser=userRepo.save(user);

        return modelMapper.map(newUser,UserDto.class);
    }

    @Override
    public void updatePassword(JwtAuthRequest jwtAuthRequest) {
        

        String email= jwtAuthRequest.getUsername();
        String password= jwtAuthRequest.getPassword();


        if (email.startsWith("\"") && email.endsWith("\"")) {
            email = email.substring(1, email.length() - 1);
        }


        System.out.println(password+" and the value of email: "+email);



       

        User user = userRepo.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("User not found with email: " ));
         System.out.println("thhe value of user is"+user);


        user.setPassword(passwordEncoder.encode(password));



        userRepo.save(user);
        



    }


    
}
