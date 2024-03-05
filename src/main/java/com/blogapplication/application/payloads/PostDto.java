package com.blogapplication.application.payloads;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import com.blogapplication.application.entity.Category;
import com.blogapplication.application.entity.Comment;
import com.blogapplication.application.entity.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class PostDto {


    private Integer postId;
    private String title;
    private String content;
    private String imageName;
    private Date addedDate;
    //User Dto data in order to avoid recursion of table
    private CategoryDto category;
    private UserDto user;
    private Set<CommentDto> comments=new HashSet<>();
   
   
    

}
