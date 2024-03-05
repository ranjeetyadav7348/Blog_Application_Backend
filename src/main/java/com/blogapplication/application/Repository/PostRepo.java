package com.blogapplication.application.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blogapplication.application.entity.Category;
import com.blogapplication.application.entity.Post;
import com.blogapplication.application.entity.User;

public interface PostRepo extends JpaRepository<Post,Integer> {

    List<Post> findByUser(User user);
    List<Post> findByCategory(Category category);
    List<Post> findByTitleContaining(String title);

    
}
