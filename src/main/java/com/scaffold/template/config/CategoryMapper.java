package com.scaffold.template.config;

import com.scaffold.template.dtos.CategoryDto;
import com.scaffold.template.dtos.CreateCategoryRequest;
import com.scaffold.template.entities.CategoryEntity;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryEntity toEntity(CategoryDto categoryDto){
        if(categoryDto == null){
            return null;
        }
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(categoryDto.getId());
        categoryEntity.setName(categoryDto.getName());
        return categoryEntity;
    }

    public CategoryDto toDto(CategoryEntity categoryEntity){
        if(categoryEntity == null){
            return null;
        }
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(categoryEntity.getId());
        categoryDto.setName(categoryEntity.getName());
        return categoryDto;
    }
}
