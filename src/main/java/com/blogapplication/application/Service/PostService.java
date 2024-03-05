package com.blogapplication.application.Service;

import java.util.List;

import com.blogapplication.application.entity.Post;
import com.blogapplication.application.payloads.PostDto;
import com.blogapplication.application.payloads.PostResponse;

public interface PostService {


    PostDto createPost(PostDto postDto,Integer userId,Integer categoryId);
    PostDto updatePost(PostDto postDto,Integer postId);
    void deletePost(Integer postId);
    PostResponse getAllPosts(Integer pageNumber,Integer pageSize,String sortBy,String sortDir);
    PostDto getPostById(Integer postId);
    List<PostDto >getPostByCategory(Integer categoryId);
    List<PostDto >getPostByUser(Integer userId);
    List <PostDto> searchPosts(String keyword);



    
}
