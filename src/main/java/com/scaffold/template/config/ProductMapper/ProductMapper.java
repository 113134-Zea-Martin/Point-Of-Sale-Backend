package com.scaffold.template.config.ProductMapper;

import com.scaffold.template.dtos.ProductCreateDTO;
import com.scaffold.template.dtos.ProductResponseDTO;
import com.scaffold.template.dtos.ProductUpdateDTO;
import com.scaffold.template.entities.BrandEntity;
import com.scaffold.template.entities.CategoryEntity;
import com.scaffold.template.entities.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductEntity toEntity(ProductCreateDTO dto, BrandEntity brand, CategoryEntity category) {
        if (dto == null) return null;

        ProductEntity entity = new ProductEntity();
        entity.setBarcode(dto.getBarcode());
        entity.setName(dto.getName());
        entity.setBrand(brand);
        entity.setCategory(category);
        entity.setPurchasePrice(dto.getPurchasePrice());
        entity.setSalePrice(dto.getSalePrice());
        entity.setCurrentStock(dto.getInitialStock());
        entity.setMinimumStock(dto.getMinimumStock());
        entity.setActive(Boolean.TRUE);
        return entity;
    }

    public void updateEntity(ProductUpdateDTO dto, ProductEntity entity, BrandEntity brand, CategoryEntity category) {
        if (dto == null || entity == null) return;
        entity.setBarcode(dto.getBarcode());
        entity.setName(dto.getName());
        if (brand != null) entity.setBrand(brand);
        if (category != null) entity.setCategory(category);
        entity.setPurchasePrice(dto.getPurchasePrice());
        entity.setSalePrice(dto.getSalePrice());
        entity.setCurrentStock(dto.getCurrentStock());
        entity.setMinimumStock(dto.getMinimumStock());
        entity.setActive(dto.getActive());
    }

    public ProductResponseDTO toResponse(ProductEntity entity) {
        if (entity == null) return null;
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(entity.getId());
        dto.setBarcode(entity.getBarcode());
        dto.setName(entity.getName());
        dto.setBrandName(entity.getBrand() != null ? entity.getBrand().getName() : null);
        dto.setCategoryName(entity.getCategory() != null ? entity.getCategory().getName() : null);
        dto.setPurchasePrice(entity.getPurchasePrice());
        dto.setSalePrice(entity.getSalePrice());
        dto.setCurrentStock(entity.getCurrentStock());
        dto.setMinimumStock(entity.getMinimumStock());
        dto.setActive(entity.getActive());
        return dto;
    }

}