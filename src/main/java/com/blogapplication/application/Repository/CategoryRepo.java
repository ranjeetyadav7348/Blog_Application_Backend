package com.blogapplication.application.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.blogapplication.application.entity.Category;

public interface CategoryRepo extends JpaRepository<Category,Integer>{



    
}
