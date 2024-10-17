
package com.blogapplication.application.Controller;

import java.security.Principal;
import java.util.Enumeration;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blogapplication.application.Exception.ApiException;
import com.blogapplication.application.Repository.UserRepository;
import com.blogapplication.application.Service.UserService;
import com.blogapplication.application.entity.User;
import com.blogapplication.application.payloads.GoogleResponse;
import com.blogapplication.application.payloads.Helper;
import com.blogapplication.application.payloads.JwtAuthRequest;
import com.blogapplication.application.payloads.JwtAuthResponse;
import com.blogapplication.application.payloads.UserDto;
import com.blogapplication.application.security.JwtTokenHelper;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/v1/auth/")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    // Autowiring the AuthenticationManager, UserDetailsService, and JwtTokenHelper
    // components
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private ConfigurableApplicationContext applicationContext;

    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Autowired
    private UserService userService;



    private final PasswordEncoder passwordEncoder;

    public AuthController(@Lazy PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    @PostMapping("/google")
    public ResponseEntity<?> loginWithGoogle(@RequestBody  GoogleResponse googleResponse ) {
      


       // System.out.println("this is a response of google: "+googleResponse);
        
       
      //  String username=null;




      User user1 = new User();

        user1.setName(googleResponse.getName());

        // user1.setId((int)Math.random());
        user1.setEmail(googleResponse.getEmail());
        user1.setAbout("This is login with google");
        user1.setPassword(passwordEncoder.encode("password"));

        user1.setPosts(null);

        User user2 = userRepository.findByEmail(googleResponse.getEmail()).orElse(null);

        if (user2 == null) {
            userRepository.save(user1);
        }

        User us2 = userRepository.findByEmail(googleResponse.getEmail()).get();


       
      
        UserDetails userDetails;
        try {
            userDetails = userDetailsService.loadUserByUsername(googleResponse.getEmail());
            if (userDetails == null) {
                System.out.println("User not found: " + googleResponse.getEmail());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            System.out.println("Error loading user by username: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }



        System.out.println("this is the usersdeatils at all: "+userDetails);

        String newToken;
        try {
            newToken = jwtTokenHelper.generateToken(userDetails);
            if (newToken == null || newToken.isEmpty()) {
                System.out.println("Error generating new token.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } catch (Exception e) {
            System.out.println("Error generating new token: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        UserDto userDto = modelMapper.map(userDetails, UserDto.class);

        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
        jwtAuthResponse.setToken(newToken);
        jwtAuthResponse.setUser(userDto);

         return ResponseEntity.status(HttpStatus.ACCEPTED).body(jwtAuthResponse);
    }

    // Endpoint for user authentication and token generation
    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> createToken(@RequestBody JwtAuthRequest request) throws Exception {

        // Authenticating the user with provided username and password
        authenticate(request.getUsername(), request.getPassword());

        // Loading UserDetails based on the authenticated username
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

        // Generating JWT token using UserDetails
        String token = jwtTokenHelper.generateToken(userDetails);

        UserDto u = modelMapper.map(userDetails, UserDto.class);
        // u.setId(u.getId());
        // u.setEmail(u.getEmail());
        // u.setName(u.getName());

        // Creating JwtAuthResponse containing the generated token
        JwtAuthResponse response = new JwtAuthResponse();
        response.setToken(token);
        response.setUser(u);

        // Returning the token in ResponseEntity with HTTP status OK
        return new ResponseEntity<JwtAuthResponse>(response, HttpStatus.OK);
    }

    // Method to authenticate user credentials using AuthenticationManager
    private void authenticate(String username, String password) throws Exception {

        // Creating an authentication token with the provided username and password
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
                password);

        try {

            // Attempting authentication with AuthenticationManager
            // verify from Dao authentication which set the details of users in Dao in
            // security config

            authenticationManager.authenticate(authenticationToken);
            System.out.println("thiiiiiiiiiiiiiiiiiiiiiiiiis is the userpasswordAuthentication Token result"
                    + authenticationToken);

        } catch (BadCredentialsException e) {
            // Handling authentication failure due to invalid credentials
            System.out.println("invalid details");
            throw new ApiException("Invalid username or password");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody UserDto userDto) {
        UserDto registeredUser = userService.registerNewUser(userDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }

    @GetMapping("/current-user/")
    public ResponseEntity<UserDto> getUser(Principal principal) {
        User user = userRepository.findByEmail(principal.getName()).get();
        return new ResponseEntity<UserDto>(this.modelMapper.map(user, UserDto.class), HttpStatus.OK);
    }

}
