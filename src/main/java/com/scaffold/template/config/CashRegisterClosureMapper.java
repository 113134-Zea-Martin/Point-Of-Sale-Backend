package com.scaffold.template.config;

import com.scaffold.template.dtos.CashRegisterClosureResponseDto;
import com.scaffold.template.entities.CashRegisterClosureEntity;
import org.springframework.stereotype.Component;

@Component
public class CashRegisterClosureMapper {

    public CashRegisterClosureResponseDto toDto(CashRegisterClosureEntity cashRegisterClosureEntity) {
        CashRegisterClosureResponseDto dto = new CashRegisterClosureResponseDto();
        dto.setDate(cashRegisterClosureEntity.getDate());
        dto.setTotalSales(cashRegisterClosureEntity.getTotalSales());
        dto.setCashTotal(cashRegisterClosureEntity.getCashTotal());
        dto.setDebitTotal(cashRegisterClosureEntity.getDebitTotal());
        dto.setCreditTotal(cashRegisterClosureEntity.getCreditTotal());
        dto.setMercadoPagoTotal(cashRegisterClosureEntity.getMercadoPagoTotal());
        dto.setTotalOperations(cashRegisterClosureEntity.getTotalOperations());
        dto.setClosedAt(cashRegisterClosureEntity.getClosedAt());
        dto.setClosedBy(cashRegisterClosureEntity.getClosedBy().getUsername());
        return dto;
    }

}
