package com.blogapplication.application.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blogapplication.application.entity.Role;

public interface RoleRepo extends JpaRepository<Role,Integer> {
    
}
