package com.blogapplication.application.config;

import com.blogapplication.application.security.CustomUserDetailsService;
import com.blogapplication.application.security.JwtAuthenticationEntryPoint;
import com.blogapplication.application.security.JwtAuthenticationFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@Component
@EnableWebSecurity
@CrossOrigin(origins = "*")
public class SecurityConfig {

    
    public static final String[] PUBLIC_URL = {
            "/api/v1/auth/**",
            "/api/v1/users/**",
            "/api/auth/**",
            "/v3/api-docs",
            "/v2/api-docs",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/webjars/**",
            "http://localhost:8080/oauth2/authorization/google",
            "http://localhost:3000/**"
    };

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private OAuthenticationSuccess oAuthenticationSuccess;

    
    // Configuring security filters and rules
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        System.out.println("+=================================================TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT");

        http
                

                
                .csrf()
                .disable()
                // Permitting access to specific endpoints without authentication
                .authorizeHttpRequests()
                
                .requestMatchers(PUBLIC_URL)
                .permitAll()
                .requestMatchers(HttpMethod.GET)
                .permitAll()
                // Requiring authentication for any other requests
                .anyRequest()
                .authenticated()
                .and()
                .exceptionHandling() // WHEN EXCEPTION IS COMES UP THEN BELOW IS PROCEEDS
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                
                // Configuring session management to be stateless
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Adding JWT authentication filter before the default
        // UsernamePasswordAuthenticationFilter

        // I THINK FIRST USERNAMEPASS..AUTH.. EXECUTE THEN JWTAUTHFILTER WILL EXECUTE
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);


        // for continue with google attempt
        
        http.oauth2Login(oauth -> {
            oauth.successHandler(oAuthenticationSuccess)
            .failureHandler(oAuthenticationSuccess);
            
            
            
            
        });

        // Building and returning the security filter chain
        return http.build();
    }

    // Encoding passwords using BCryptPasswordEncoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Configuring DAO Authentication Provider
    @Bean
    public DaoAuthenticationProvider daoauthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        // it setted the the user detail in authentication providers

        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }




    // Providing Authentication Manager bean
    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration confi) throws Exception {
        return confi.getAuthenticationManager();
    }

    // @Bean
    // public FilterRegistrationBean coresFilter() {
    //     UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

    //     CorsConfiguration corsConfiguration = new CorsConfiguration();
    //     corsConfiguration.setAllowCredentials(true);
    //     corsConfiguration.addAllowedOriginPattern("http://localhost:3000");
    //     corsConfiguration.addAllowedHeader("Authorization");
    //     corsConfiguration.addAllowedHeader("Content-Type");
    //     corsConfiguration.addAllowedHeader("Accept");
    //     corsConfiguration.addAllowedMethod("POST");
    //     corsConfiguration.addAllowedMethod("GET");
    //     corsConfiguration.addAllowedMethod("DELETE");
    //     corsConfiguration.addAllowedMethod("PUT");
    //     corsConfiguration.addAllowedMethod("OPTIONS");
    //     corsConfiguration.setMaxAge(3600L);

    //     source.registerCorsConfiguration("/**", corsConfiguration);

    //     FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));

    //     bean.setOrder(-110);

    //     return bean;
    // }

}
