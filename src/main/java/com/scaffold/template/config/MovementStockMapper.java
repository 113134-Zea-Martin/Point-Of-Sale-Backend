package com.scaffold.template.config;

import com.scaffold.template.dtos.MovementStockCreateDto;
import com.scaffold.template.dtos.MovementStockResponseDto;
import com.scaffold.template.entities.MovementStockEntity;
import com.scaffold.template.entities.ProductEntity;
import com.scaffold.template.entities.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class MovementStockMapper {

    public MovementStockEntity toEntity(MovementStockCreateDto dto, ProductEntity productEntity, UserEntity userEntity) {
        if (dto == null) return null;
        if (productEntity == null) return null;
        if (userEntity == null) return null;

        MovementStockEntity entity = new MovementStockEntity();
        entity.setProduct(productEntity);
        entity.setMovementType(dto.getMovementType());
        entity.setReason(dto.getReason());
        entity.setQuantity(dto.getQuantity());
        entity.setDate(dto.getDate());
        entity.setUser(userEntity);
        return entity;
    }

    public MovementStockResponseDto toMovementStockResponseDto(MovementStockEntity movementStockEntity) {
        if (movementStockEntity == null) return null;

        MovementStockResponseDto dto = new MovementStockResponseDto();
        dto.setId(movementStockEntity.getId());
        dto.setProductId(movementStockEntity.getProduct().getId());
        dto.setProductName(movementStockEntity.getProduct().getName());
        dto.setMovementType(movementStockEntity.getMovementType());
        dto.setReason(movementStockEntity.getReason());
        dto.setQuantity(movementStockEntity.getQuantity());
        dto.setDate(movementStockEntity.getDate());
        dto.setUserId(movementStockEntity.getUser().getId());
        return dto;
    }
}
