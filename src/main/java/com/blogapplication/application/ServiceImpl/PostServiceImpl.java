package com.blogapplication.application.ServiceImpl;


import java.util.Date;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.blogapplication.application.Exception.ResourceNotFoundException;
import com.blogapplication.application.Repository.CategoryRepo;
import com.blogapplication.application.Repository.PostRepo;
import com.blogapplication.application.Repository.UserRepository;
import com.blogapplication.application.Service.PostService;
import com.blogapplication.application.entity.Category;
import com.blogapplication.application.entity.Post;
import com.blogapplication.application.entity.User;
import com.blogapplication.application.payloads.PostDto;
import com.blogapplication.application.payloads.PostResponse;


@Component
public class PostServiceImpl implements PostService {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PostRepo postRepo;

    @Autowired
    private UserRepository userRepository;


    @Autowired 
    private CategoryRepo categoryRepo;


    @Override
    public PostDto createPost(PostDto postDto,Integer userId,Integer categoryId) {
        // TODO Auto-generated method stub


        Category cat=categoryRepo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category","CategoryId",categoryId));

        User user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","userId",userId));

        Post post=modelMapper.map(postDto,Post.class);

        post.setImageName("dafault.png");

        java.util.Date utilDate = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        post.setAddedDate(sqlDate);

        post.setCategory(cat);
        post.setUser(user);


        Post pt=postRepo.save(post);

        return modelMapper.map(pt,PostDto.class);
    }

    @Override
    public void deletePost(Integer postId) {
       
        Post post=postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","postId",postId));
        
          postRepo.delete(post);
    }

    @Override
    public PostResponse getAllPosts(Integer pageNumber,Integer pageSize,String sortBy,String sortDir) {

        Sort sort=null;

        if(sortDir.equalsIgnoreCase("asc"))
        {
           sort=Sort.by(sortBy).ascending();
        }
        else  sort=Sort.by(sortBy).descending();


        Pageable p= PageRequest.of(pageNumber, pageSize,sort);
        Page< Post > pagePosts=postRepo.findAll(p);
        List<Post> allPosts =pagePosts.getContent();

        List<PostDto> list=allPosts.stream().map((post)->modelMapper.map(post,PostDto.class)).collect(Collectors.toList());
         PostResponse postResponse=new PostResponse();
         postResponse.setContent(list);
         postResponse.setLastPage(pagePosts.isLast());
         postResponse.setPageNumber(pagePosts.getNumber());
         postResponse.setTotalElements(pagePosts.getTotalElements());
         postResponse.setTotalPages(pagePosts.getTotalPages());
         postResponse.setPageSize(pagePosts.getSize());

       
        return postResponse;

    }



    @Override
    public List<PostDto> getPostByCategory(Integer categoryId) {


        Category cat=categoryRepo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category","CategoryId",categoryId));

        List<Post> posts=postRepo.findByCategory(cat);
        List<PostDto> postDtos=posts.stream().map((post)->modelMapper.map(post,PostDto.class)).collect(Collectors.toList());

       
        return postDtos;
    }


    //GET POST BY ID
    @Override
    public PostDto getPostById(Integer postId) {

        Post post=postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","PostId",postId));




        return modelMapper.map(post,PostDto.class);
    }



    @Override
    public List<PostDto> getPostByUser(Integer userId) {
       User user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","UserId",userId));
        
       List<Post> posts=postRepo.findByUser(user);

       List<PostDto> list=posts.stream().map((post)->modelMapper.map(post,PostDto.class)).collect(Collectors.toList());
       
       return list;
    }

    @Override
    public List<PostDto> searchPosts(String keyword) {
        // TODO Auto-generated method stub

        List<Post> posts=postRepo.findByTitleContaining(keyword);
        List<PostDto> list=posts.stream().map((post)->modelMapper.map(post,PostDto.class)).collect(Collectors.toList());
        
        return list;
    }

    @Override
    public PostDto updatePost(PostDto postDto, Integer postId) {
      Post post=postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","PostId",postId));

    //   post.setAddedDate(postDto.getAddedDate());
     
      post.setContent(postDto.getContent());
      post.setImageName(postDto.getImageName());
      post.setTitle(postDto.getTitle());

      Post updatedPost=postRepo.save(post);

        return modelMapper.map(updatedPost,PostDto.class);
    }
    
    
}
