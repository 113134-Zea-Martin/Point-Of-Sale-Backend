package com.scaffold.template.services;

import com.scaffold.template.dtos.CategoryDto;
import com.scaffold.template.dtos.CreateCategoryRequest;
import com.scaffold.template.entities.CategoryEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
    CategoryEntity getCategoryById(Long id);

    List<CategoryDto> getAllCategories();
    CategoryDto createCategory(CreateCategoryRequest createCategoryRequest);
}
