package com.blogapplication.application.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blogapplication.application.Service.CommentService;
import com.blogapplication.application.payloads.CommentDto;
import com.blogapplication.application.payloads.apiResponse;

@RestController
@RequestMapping("/api/")


public class CommentController {


    @Autowired
    private CommentService commentService;

    @PostMapping("/post/{postId}/comments")
    public ResponseEntity<CommentDto> createComment(@RequestBody CommentDto commentDto,@PathVariable Integer postId)
    {
             CommentDto commentDto2=commentService.createComment(commentDto, postId);
             return ResponseEntity.status(HttpStatus.OK).body(commentDto2);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<apiResponse> deleteComment(@PathVariable Integer commentId)
    {
             commentService.deleteComment(commentId);
             return new ResponseEntity< apiResponse >(new  apiResponse("Comment Deleted successfully",true),(HttpStatus.ACCEPTED));
    }
    
}
