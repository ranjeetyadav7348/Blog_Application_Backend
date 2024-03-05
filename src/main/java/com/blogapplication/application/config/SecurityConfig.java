// package com.blogapplication.application.config;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
// import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// import com.blogapplication.application.security.CustomUserDetailsService;
// import com.blogapplication.application.security.JwtAuthenticationEntryPoint;
// import com.blogapplication.application.security.JwtAuthenticationFilter;

// import io.jsonwebtoken.security.Password;

// public class SecurityConfig extends WebSecurityConfigurerAdapter {

//     @Autowired
//     private CustomUserDetailsService customUserDetailsService;

//     @Autowired
//     private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

//     @Autowired
//     private JwtAuthenticationFilter jwtAuthenticationFilter;

//     @Override
//     protected void configure (HttpSecurity http) throws Exception
//     {
//           http.
//               csrf().
//               disable().
//               authorizeHttpRequests().
//               antMatchers("/api/v1/auth/login").permitAll().
//               anyRequest().
//               authenticated().
//               and().
//             exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).
//               sessionManagement().
//               sessionCreationPolicy(SessionCreationPolicy.STATELESS);

//               http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

//     }

//     @Override
//     protected void configure (AuthenticationManagerBuilder auth) throws Exception
//     {
//         auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());

//     }

//     @Bean
//     public PasswordEncoder passwordEncoder()
//     {
//         return new BCryptPasswordEncoder();
//     }

//     @Bean
//     @Override
//     public AuthenticationManager authenticationManagerBean() throws Exception
//     {
//             return super.authenticationManagerBean();
//     }

// }

// package com.blogapplication.application.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;

// @Configuration

// public class SecurityConfig{

//     @Bean 
//     public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception
//     {
//         httpSecurity
//         .csrf()
//         .disable()
//         .authorizeRequests()
//         .requestMatchers("/api/v1/auth/login")
//         .permitAll()
//         .and()
//         .httpBasic();
//         return httpSecurity.build();

//     }

//     @Override
//     protected void configure (AuthenticationManagerBuilder auth) throws Exception
//     {
//         auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());

//     }

//     @Bean
//     public PasswordEncoder passwordEncoder()
//     {
//         return new BCryptPasswordEncoder();
//     }

// }

package com.blogapplication.application.config;

import com.blogapplication.application.security.CustomUserDetailsService;
import com.blogapplication.application.security.JwtAuthenticationEntryPoint;
import com.blogapplication.application.security.JwtAuthenticationFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Configuration
@Component
@EnableWebSecurity
@EnableMethodSecurity

public class SecurityConfig {

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
  
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers("/api/v1/auth/login")
                .permitAll()
                .requestMatchers(HttpMethod.GET)
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);


     //  http.authenticationProvider(daoauthenticationProvider());
        return http.build();
    }

    // @Autowired
    // public void configureGlobal(AuthenticationManagerBuilder auth) throws
    // Exception {
    // auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
    // }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoauthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

   

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration confi) throws Exception {
        return confi.getAuthenticationManager();
    }

}

