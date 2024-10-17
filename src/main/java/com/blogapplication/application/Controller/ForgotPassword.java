package com.blogapplication.application.Controller;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blogapplication.application.Service.GamilService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("api/v1/auth/forgotPassword")
public class ForgotPassword {


    @Autowired
    private GamilService gamilService;

    @PostMapping("/send-otp")
    public Integer sendOtp(@RequestBody String email)
    {

        Random random= new Random(1000);
        Integer otp= random.nextInt(9999);
        gamilService.sendEmail("viratyadav9415@gmail.com","This is subject","1233");
     

        return otp;
 

    }
    
}
