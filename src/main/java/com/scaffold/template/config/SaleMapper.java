package com.scaffold.template.config;

import com.scaffold.template.dtos.SaleCreateDto;
import com.scaffold.template.dtos.SaleDetailDto;
import com.scaffold.template.dtos.SaleResponseDto;
import com.scaffold.template.entities.SaleDetailEntity;
import com.scaffold.template.entities.SaleEntity;
import org.springframework.stereotype.Component;

@Component
public class SaleMapper {

    public SaleResponseDto map(SaleEntity saleEntity) {
        SaleResponseDto dto = new SaleResponseDto();
        dto.setDateTime(saleEntity.getDateTime());
        dto.setTotal(saleEntity.getTotal());
        dto.setPaymentType(saleEntity.getPaymentType().name());
        dto.setUserId(saleEntity.getUser().getId());
        dto.setSaleDetails(
                saleEntity.getSaleDetails().stream()
                        .map(this::toSaleDetailDto)
                        .toList()
        );
        return dto;
    }

    public SaleDetailDto toSaleDetailDto(SaleDetailEntity saleDetailEntity) {
        SaleDetailDto dto = new SaleDetailDto();
        dto.setBarcode(saleDetailEntity.getProduct().getBarcode());
        dto.setQuantity(saleDetailEntity.getQuantity());
        return dto;
    }

    public SaleDetailEntity toSaleDetailEntity(SaleDetailDto saleDetailDto) {
        SaleDetailEntity entity = new SaleDetailEntity();
        entity.setQuantity(saleDetailDto.getQuantity());
        return entity;
    }

}
