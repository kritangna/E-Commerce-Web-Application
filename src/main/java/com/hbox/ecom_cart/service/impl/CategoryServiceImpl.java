package com.hbox.ecom_cart.service.impl;

import com.hbox.ecom_cart.dto.CategoryDto;
import com.hbox.ecom_cart.entity.Category;
import com.hbox.ecom_cart.exception.EcomCartException;
import com.hbox.ecom_cart.repositoty.CategoryRepository;
import com.hbox.ecom_cart.service.CategoryService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;
    private ModelMapper modelMapper;

    @Override
    public CategoryDto addCategory(CategoryDto categoryDto) {

        Category category = new Category();
        category.setCategoryName(categoryDto.getCategoryName());

        return modelMapper.map(categoryRepository.save(category), CategoryDto.class);
    }

    @Override
    public CategoryDto getCategoryById(Long id) {

        Category existingCategory = categoryRepository.findById(id).orElseThrow(() ->
                new EcomCartException(HttpStatus.BAD_REQUEST, "Category not found"));

        return modelMapper.map(existingCategory, CategoryDto.class);
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDto> categoryDtos = categories.stream().map(category ->
                modelMapper.map(category, CategoryDto.class))
                .collect(Collectors.toList());
        return categoryDtos;
    }

    @Override
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        Category existingCategory = categoryRepository.findById(id).orElseThrow(() ->
                new EcomCartException(HttpStatus.BAD_REQUEST, "Category not found"));
        if(categoryDto.getCategoryName() != null && !categoryDto.getCategoryName().equals(existingCategory.getCategoryName())) {
            existingCategory.setCategoryName(categoryDto.getCategoryName());
        }
        return modelMapper.map(categoryRepository.save(existingCategory), CategoryDto.class);
    }

    @Override
    public void deleteCategoryById(Long id) {
        Category existingCategory = categoryRepository.findById(id).orElseThrow(() ->
                new EcomCartException(HttpStatus.BAD_REQUEST, "Category not found"));
        categoryRepository.delete(existingCategory);
    }
}
