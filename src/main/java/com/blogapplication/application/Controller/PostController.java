package com.blogapplication.application.Controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.blogapplication.application.Service.FileService;
import com.blogapplication.application.Service.PostService;
import com.blogapplication.application.config.AppConstant;
import com.blogapplication.application.entity.Post;
import com.blogapplication.application.payloads.PostDto;
import com.blogapplication.application.payloads.PostResponse;
import com.blogapplication.application.payloads.apiResponse;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired 
    private FileService fileService;


    @Value("${project.image}")
    private String path;
    // CREATE
    @PostMapping("/user/{userId}/category/{categoryId}/posts")
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto,
            @PathVariable Integer userId,
            @PathVariable Integer categoryId) {
        PostDto created = postService.createPost(postDto, userId, categoryId);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/user/{userId}/posts")

    public ResponseEntity<List<PostDto>> getPostsByUser(@PathVariable Integer userId) {
        List<PostDto> list = postService.getPostByUser(userId);

        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @GetMapping("/category/{categoryId}/posts")

    public ResponseEntity<List<PostDto>> getPostsByCategory(@PathVariable Integer categoryId) {
        List<PostDto> list = postService.getPostByCategory(categoryId);

        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @GetMapping("/posts")
    public ResponseEntity<PostResponse> getAllPost(
        @RequestParam(value = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
        @RequestParam(value = "pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
        @RequestParam(value = "sortBy", defaultValue = AppConstant.SORT_BY,required = false) String sortBy,
        @RequestParam(value = "sortDir", defaultValue = AppConstant.SORT_DIR,required = false) String sortDir
    ) {
         PostResponse list = postService.getAllPosts(pageNumber,pageSize,sortBy,sortDir);
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @GetMapping("posts/{postId}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Integer postId) {
        PostDto list = postService.getPostById(postId);
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @DeleteMapping("posts/{postId}")
    public apiResponse deletePostById(@PathVariable Integer postId)
    {
       postService.deletePost(postId);
        return new apiResponse("Post is deleted successfully", true);
    }

    @PutMapping("posts/{postId}")
    public ResponseEntity<PostDto> updatePostById(@RequestBody PostDto postDto ,@PathVariable Integer postId)
    {
       PostDto postDto2= postService.updatePost(postDto, postId);
     
        return ResponseEntity.status(HttpStatus.OK).body(postDto2);
    }

@GetMapping("/posts/search/{keyword}")
    public ResponseEntity<List<PostDto>> searchBytitle(@PathVariable String keyword)
    {
        List<PostDto> serachList=postService.searchPosts(keyword);
        return ResponseEntity.status(HttpStatus.OK).body(serachList);
    }



    @PostMapping("/post/image/upload/{postId}")
    public ResponseEntity<PostDto> uploadPostImage(@RequestBody MultipartFile image,@PathVariable Integer postId) throws IOException
    {


        PostDto postDto = postService.getPostById(postId);
        String fileName= fileService.uploadImage(path, image);
       

        postDto.setImageName(fileName);
        PostDto updatePost=postService.updatePost(postDto, postId);

        return ResponseEntity.status(HttpStatus.OK).body(updatePost);

    }


    @GetMapping(value = "/post/image/{imageName}",produces = MediaType.IMAGE_JPEG_VALUE)
    public void downloaImage(@PathVariable String imageName,
    HttpServletResponse response
    ) throws IOException
    {
     InputStream resource= fileService.getResource(path, imageName);
     response.setContentType(MediaType.IMAGE_JPEG_VALUE);
     StreamUtils.copy(resource, response.getOutputStream());

    }


    
}
