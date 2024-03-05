package com.blogapplication.application.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blogapplication.application.entity.User;

public interface UserRepository extends JpaRepository<User,Integer> {
    

    Optional<User> findByEmail(String email);

}
