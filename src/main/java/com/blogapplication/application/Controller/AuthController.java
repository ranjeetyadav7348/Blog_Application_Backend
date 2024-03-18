// package com.blogapplication.application.Controller;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.BadCredentialsException;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import com.blogapplication.application.payloads.JwtAuthRequest;
// import com.blogapplication.application.payloads.JwtAuthResponse;
// import com.blogapplication.application.security.JwtTokenHelper;

// @RestController
// @RequestMapping("/api/v1/auth/")
// public class AuthController {


//     @Autowired
//     private AuthenticationManager authenticationManager;

//     @Autowired
//     private UserDetailsService userDetailsService;

//     @Autowired 
//     private JwtTokenHelper jwtTokenHelper;

//     @PostMapping("/login")
//     public ResponseEntity<JwtAuthResponse> createToken
//     (
//         @RequestBody JwtAuthRequest request
//     ) throws Exception
//     {

//                authenticate(request.getUsername(),request.getPassword());
              
//                UserDetails userDetails=userDetailsService.loadUserByUsername(request.getUsername());

              

//                String token= jwtTokenHelper.generateToken(userDetails);

             


//                JwtAuthResponse response= new JwtAuthResponse();
              
//                response.setToken(token);
//                return new ResponseEntity<JwtAuthResponse>(response,HttpStatus.OK);
        
//     }

//     private void authenticate(String username, String password) throws Exception {
//         // TODO Auto-generated method stub

//         UsernamePasswordAuthenticationToken authenticationToken= new UsernamePasswordAuthenticationToken(username, password);

//        try {

//        System.out.println("virat");
//        authenticationManager.authenticate(authenticationToken);

//        } catch (BadCredentialsException e) {
     

//         System.out.println("invalid details");
//         throw new Exception("Invalid username or password");
//        }

        
//     }
    
// }

















package com.blogapplication.application.Controller;

import java.security.Principal;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blogapplication.application.Exception.ApiException;
import com.blogapplication.application.Repository.UserRepository;
import com.blogapplication.application.Service.UserService;
import com.blogapplication.application.entity.User;
import com.blogapplication.application.payloads.JwtAuthRequest;
import com.blogapplication.application.payloads.JwtAuthResponse;
import com.blogapplication.application.payloads.UserDto;
import com.blogapplication.application.security.JwtTokenHelper;

@RestController
@RequestMapping("/api/v1/auth/")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    // Autowiring the AuthenticationManager, UserDetailsService, and JwtTokenHelper components
    @Autowired
    private  ModelMapper modelMapper;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired 
    private JwtTokenHelper jwtTokenHelper;

    @Autowired private UserService userService;

    // Endpoint for user authentication and token generation
    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> createToken(@RequestBody JwtAuthRequest request) throws Exception {

        // Authenticating the user with provided username and password
        authenticate(request.getUsername(),request.getPassword());

        
              
        // Loading UserDetails based on the authenticated username
        UserDetails userDetails=userDetailsService.loadUserByUsername(request.getUsername());



        
        // Generating JWT token using UserDetails
        String token= jwtTokenHelper.generateToken(userDetails);

       

        UserDto u=modelMapper.map(userDetails,UserDto.class);
        // u.setId(u.getId());
        // u.setEmail(u.getEmail());
        // u.setName(u.getName());



     //   System.out.println("this is user details om guys chai pilo: "+u.getEmail());



        // Creating JwtAuthResponse containing the generated token
        JwtAuthResponse response= new JwtAuthResponse();
        response.setToken(token);
        response.setUser(u);
        
        // Returning the token in ResponseEntity with HTTP status OK
        return new ResponseEntity<JwtAuthResponse>(response,HttpStatus.OK);
    }

    // Method to authenticate user credentials using AuthenticationManager
    private void authenticate(String username, String password) throws Exception {

        // Creating an authentication token with the provided username and password
        UsernamePasswordAuthenticationToken authenticationToken= new UsernamePasswordAuthenticationToken(username, password);

      

        try {
            
            // Attempting authentication with AuthenticationManager
            // verify from Dao authentication which set the details of users in Dao in security config

            authenticationManager.authenticate(authenticationToken);
            System.out.println("thiiiiiiiiiiiiiiiiiiiiiiiiis is the userpasswordAuthentication Token result"+ authenticationToken);

        } catch (BadCredentialsException e) {
            // Handling authentication failure due to invalid credentials
            System.out.println("invalid details");
            throw new ApiException("Invalid username or password");
        }
    }  



    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody UserDto userDto)
    {
        UserDto registeredUser=userService.registerNewUser(userDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }


    @GetMapping("/current-user/")
	public ResponseEntity<UserDto> getUser(Principal principal) {
		User user = userRepository.findByEmail(principal.getName()).get();
		return new ResponseEntity<UserDto>(this.modelMapper.map(user, UserDto.class), HttpStatus.OK);
	}




}

