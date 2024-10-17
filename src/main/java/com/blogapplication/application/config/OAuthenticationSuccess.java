package com.blogapplication.application.config;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.apache.hc.core5.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.blogapplication.application.Repository.RoleRepo;
import com.blogapplication.application.Repository.UserRepository;
import com.blogapplication.application.entity.Role;
import com.blogapplication.application.entity.User;
import com.blogapplication.application.payloads.UserDto;
import com.blogapplication.application.security.JwtTokenHelper;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
@CrossOrigin(origins = "*")
public class OAuthenticationSuccess implements AuthenticationSuccessHandler, AuthenticationFailureHandler {

    Logger logger = LoggerFactory.getLogger(OAuthenticationSuccess.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Autowired
    private ConfigurableApplicationContext applicationContext;

    private final PasswordEncoder passwordEncoder;

    public OAuthenticationSuccess(@Lazy PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

 
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();


        String email = user.getAttribute("email").toString();
        String name = user.getAttribute("name").toString();
        String picture = user.getAttribute("picture").toString();

        System.out.println(email + " name is: " + name + " pic is: " + picture);

        User user1 = new User();

        user1.setName(name);

        // user1.setId((int)Math.random());
        user1.setEmail(email);
        user1.setAbout("This is login with google");
        user1.setPassword(passwordEncoder.encode("password"));

        user1.setPosts(null);

        User user2 = userRepository.findByEmail(email).orElse(null);

        if (user2 == null) {
            userRepository.save(user1);
        }

        User us2 = userRepository.findByEmail(email).orElse(null);

        String token = jwtTokenHelper.generateToken(us2);


        // Store the token in the session
        HttpSession session = request.getSession();
        session.setAttribute("JWT_TOKEN", token);
    
        // Redirect to the endpoint with a query parameter (if needed)
        new DefaultRedirectStrategy().sendRedirect(request, response, "http://localhost:8080/api/v1/auth/google");

       

    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {

        System.out.println("this is the exception" + exception);

        new DefaultRedirectStrategy().sendRedirect(request, response, "http://localhost:3000/login");
        

    }

}
