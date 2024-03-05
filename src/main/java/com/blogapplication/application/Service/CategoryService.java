package com.blogapplication.application.Service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.blogapplication.application.payloads.CategoryDto;



public interface CategoryService {


    //CREATE

    CategoryDto createCategory(CategoryDto categoryDto);
    CategoryDto updateCategory(CategoryDto categoryDto,Integer categoryId);
    void deleteCategory(Integer categoryId);
    CategoryDto getCategory(Integer categoryId);
    List<CategoryDto> getAllCategories();


    
}
