package com.blogapplication.application.payloads;

import java.security.Principal;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

public class Helper {



    public static String getEmailOfLoggedIn(Authentication authentication)
    {


       // AuthenticationPrincipal principal= (AuthenticationPrincipal ) authentication.getPrincipal();
        if(authentication instanceof OAuth2AuthenticatedPrincipal)
        {
                 var OAuth=(OAuth2AuthenticationToken)authentication;

                 
        }
        return authentication.getName();
    }




    
}
