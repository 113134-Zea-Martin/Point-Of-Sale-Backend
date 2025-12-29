package com.scaffold.template.services;

import com.scaffold.template.dtos.MovementStockCreateDto;
import com.scaffold.template.entities.MovementStockEntity;
import org.springframework.stereotype.Service;

@Service
public interface MovementStockService {

    MovementStockEntity recordMovement(MovementStockCreateDto movementStockCreateDto);

}
