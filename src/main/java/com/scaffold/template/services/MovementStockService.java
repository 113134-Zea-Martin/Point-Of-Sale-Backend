package com.scaffold.template.services;

import com.scaffold.template.dtos.MovementStockCreateDto;
import com.scaffold.template.dtos.MovementStockResponseDto;
import com.scaffold.template.entities.MovementStockEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface MovementStockService {

    MovementStockEntity recordMovement(MovementStockCreateDto movementStockCreateDto);

    Page<MovementStockResponseDto> findMovementssByFilters(
            String productName,
            String movementType,
            String reason,
            Long userId,
            String fromDate,
            String toDate,
            Pageable pageable
    );

}
