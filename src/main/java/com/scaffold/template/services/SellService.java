package com.scaffold.template.services;

import com.scaffold.template.dtos.SaleCreateDto;
import com.scaffold.template.dtos.SaleResponseDto;
import com.scaffold.template.dtos.SaleResponseWithNameDto;
import com.scaffold.template.entities.SaleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SellService {
    SaleResponseDto save(SaleCreateDto saleCreateDto);

    Page<SaleResponseWithNameDto> findSalesByFilters(Long userId,
                                                     String paymentType,
                                                     String fromDate,
                                                     String toDate,
                                                     Double minTotal,
                                                     Double maxTotal,
                                                     Pageable pageable);

}
