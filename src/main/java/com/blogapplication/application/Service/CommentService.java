package com.blogapplication.application.Service;

import com.blogapplication.application.payloads.CommentDto;

public interface CommentService {

    CommentDto createComment(CommentDto commentDto,Integer postId);
    void deleteComment(Integer commentId);

    
}
