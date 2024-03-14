package com.blogapplication.application;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.blogapplication.application.Repository.RoleRepo;
import com.blogapplication.application.entity.Role;

@SpringBootApplication
public class Application implements CommandLineRunner {


	// @Autowired
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
		
		
		try{
		Role role=new Role();
		role.setId(1);
		role.setName("NORMAL_USER");

		Role role1=new Role();
		role1.setId(2);
		role1.setName("ADMIN_USER");

		List <Role> roles= List.of(role,role1);

		List<Role> result= roleRepo.saveAll(roles);
		}
		catch (Exception e)
		{
            e.printStackTrace();
		}
	}

	@Bean
	public ModelMapper modelMapper()
	{
		return new ModelMapper();
	}

}
