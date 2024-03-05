package com.blogapplication.application.ServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.blogapplication.application.Exception.ResourceNotFoundException;
import com.blogapplication.application.Repository.CategoryRepo;
import com.blogapplication.application.Service.CategoryService;
import com.blogapplication.application.entity.Category;
import com.blogapplication.application.payloads.CategoryDto;



@Component
public class CategoryServiceImpl implements CategoryService {


    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        
        Category category=modelMapper.map(categoryDto,Category.class);
         Category cat=categoryRepo.save(category);
      
        return modelMapper.map(cat,CategoryDto.class);
    }

    @Override
    public void deleteCategory(Integer categoryId) {
       
        Category cat=categoryRepo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category","CategoryId",categoryId));

        categoryRepo.delete(cat);
       // return ResponseEntity.;
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        // TODO Auto-generated method stub

        List<Category> cat=categoryRepo.findAll();

        List<CategoryDto> list=cat.stream().map(user->modelMapper.map(user,CategoryDto.class)).collect(Collectors.toList());

        return list;
    }

    @Override
    public CategoryDto getCategory(Integer categoryId) {
       Category cat=categoryRepo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category","CategoryId",categoryId));



        return modelMapper.map(cat,CategoryDto.class);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, Integer categoryId) {
       

        Category cat=categoryRepo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category","CategoryId",categoryId));
         

        cat.setCategoryTilte(categoryDto.getCategoryTilte());
        cat.setCategoryDescription(categoryDto.getCategoryDescription());


        Category upadtedCat=categoryRepo.save(cat);

        return modelMapper.map(upadtedCat,CategoryDto.class);
    }
    
}
