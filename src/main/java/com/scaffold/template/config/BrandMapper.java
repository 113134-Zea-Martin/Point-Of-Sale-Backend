package com.scaffold.template.config;

import com.scaffold.template.dtos.BrandDto;
import com.scaffold.template.entities.BrandEntity;
import org.springframework.stereotype.Component;

@Component
public class BrandMapper {

    public BrandEntity toEntity(BrandDto brandDto) {
        if (brandDto == null) {
            return null;
        }
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setId(brandDto.getId());
        brandEntity.setName(brandDto.getName());
        return brandEntity;
    }

    public BrandDto toDto(BrandEntity brandEntity) {
        if (brandEntity == null) {
            return null;
        }
        return new BrandDto(brandEntity.getId(), brandEntity.getName());
    }
}
