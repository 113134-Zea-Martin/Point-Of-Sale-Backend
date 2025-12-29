package com.scaffold.template.services.impl;

import com.scaffold.template.config.CategoryMapper;
import com.scaffold.template.dtos.CategoryDto;
import com.scaffold.template.dtos.CreateCategoryRequest;
import com.scaffold.template.entities.CategoryEntity;
import com.scaffold.template.repositories.CategoryRepository;
import com.scaffold.template.services.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }


    @Override
    public CategoryEntity getCategoryById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El id de la categoría no puede ser nulo");
        }
        return categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoria no encontrada con id: " + id));
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        List<CategoryEntity> categoryEntities = categoryRepository.findAll();
        return categoryEntities.stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    public CategoryDto createCategory(CreateCategoryRequest createCategoryRequest) {
        if (createCategoryRequest == null || createCategoryRequest.getName() == null || createCategoryRequest.getName().isBlank()) {
            throw new IllegalArgumentException("El nombre de la categoría no puede ser nulo o vacío");
        }
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName(createCategoryRequest.getName());
        CategoryEntity savedEntity = categoryRepository.save(categoryEntity);
        return categoryMapper.toDto(savedEntity);
    }
}
