package com.blogapplication.application.ServiceImpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.blogapplication.application.Exception.ResourceNotFoundException;
import com.blogapplication.application.Repository.CommentRepo;
import com.blogapplication.application.Repository.PostRepo;
import com.blogapplication.application.Service.CommentService;
import com.blogapplication.application.entity.Comment;
import com.blogapplication.application.entity.Post;
import com.blogapplication.application.payloads.CommentDto;


@Component
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepo commentRepo;
    @Autowired
    private PostRepo postRepo;

     @Autowired
    private ModelMapper modelMapper;

    @Override
    public CommentDto createComment(CommentDto commentDto, Integer postId) {
        Post post=postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","PostId",postId));

        Comment comment=modelMapper.map(commentDto,Comment.class);

        comment.setPost(post);

       Comment cmt= commentRepo.save(comment);

        return modelMapper.map(cmt,CommentDto.class);
    }

    @Override
    public void deleteComment(Integer commentId) {
        // TODO Auto-generated method stub


        Comment comment=commentRepo.findById(commentId).orElseThrow(()->new ResourceNotFoundException("Comment","commentId",commentId));
        commentRepo.delete(comment);

    }
    
}
