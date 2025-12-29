package com.scaffold.template.services;

import com.scaffold.template.dtos.SaleCreateDto;
import com.scaffold.template.dtos.SaleResponseDto;
import com.scaffold.template.entities.SaleEntity;
import org.springframework.stereotype.Service;

@Service
public interface SellService {
    SaleResponseDto save(SaleCreateDto saleCreateDto);

}
