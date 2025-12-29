package com.scaffold.template.controllers;

import com.scaffold.template.dtos.CategoryDto;
import com.scaffold.template.dtos.CreateCategoryRequest;
import com.scaffold.template.entities.CategoryEntity;
import com.scaffold.template.services.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/categories")
@Tag(name = "Categorías", description = "API para gestión de categorías")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("")
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        try {
            List<CategoryDto> categories = categoryService.getAllCategories();
            if (categories.isEmpty()) {
                log.info("No se encontraron categorías");
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            log.error("Error al obtener las categorías: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("")
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CreateCategoryRequest createCategoryRequest) {
        try {
            CategoryDto newCategory = categoryService.createCategory(createCategoryRequest);
            return ResponseEntity.status(201).body(newCategory);
        } catch (Exception e) {
            log.error("Error al crear la categoría: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

}
