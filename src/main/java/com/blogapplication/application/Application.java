package com.blogapplication.application;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.blogapplication.application.Repository.RoleRepo;
import com.blogapplication.application.Service.GamilService;
import com.blogapplication.application.entity.Role;

@SpringBootApplication
@EnableFeignClients

@CrossOrigin(origins = "http://localhost:3000")
public class Application implements CommandLineRunner {

	// @Autowired
	// private GamilService gamilService;
	// private PasswordEncoder passwordEncoder;

	@Autowired
	private Role role;
	@Autowired
	private RoleRepo roleRepo;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		try {
			Role role = new Role();
			role.setId(1);
			role.setName("NORMAL_USER");

			Role role1 = new Role();
			role1.setId(2);
			role1.setName("ADMIN_USER");

			List<Role> roles = List.of(role, role1);

			List<Role> result = roleRepo.saveAll(roles);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	

	// @EventListener(ApplicationReadyEvent.class)
	// public void sendMail(){
	// gamilService.sendEmail("viratyadav9415@gmail.com","This is subject","This is
	// email body");
	// }

}
