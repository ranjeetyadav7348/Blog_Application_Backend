package com.blogapplication.application.Controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blogapplication.application.Service.CategoryService;
import com.blogapplication.application.payloads.CategoryDto;
import com.blogapplication.application.payloads.apiResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    @PostMapping("/")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto)
    {
        CategoryDto cat=categoryService.createCategory(categoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(cat);
    }


    @PostMapping("/{catId}")
    public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto,@PathVariable Integer catId)
    {
        CategoryDto updatedCat=categoryService.updateCategory(categoryDto, catId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedCat);
    }

    @DeleteMapping("/{catId}")
    public ResponseEntity<apiResponse> deleteCategory(@PathVariable Integer catId)
    {
       categoryService.deleteCategory(catId);
        return new ResponseEntity(new apiResponse("Category Deleted Successfully",true),HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<CategoryDto>> getAllCategory()
    {
        List <CategoryDto> updatedCat=categoryService.getAllCategories();
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedCat);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> get_Category(@PathVariable Integer categoryId)
    {
        CategoryDto  updatedCat=categoryService.getCategory(categoryId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedCat);
    }

    


    
}
