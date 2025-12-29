package com.scaffold.template.services;

import com.scaffold.template.dtos.InputStockDto;
import com.scaffold.template.dtos.MovementStockResponseDto;
import com.scaffold.template.entities.MovementStockEntity;
import org.springframework.stereotype.Service;

@Service
public interface StockService {

    MovementStockResponseDto stockIn(InputStockDto inputStockDto);

    MovementStockResponseDto stockOut(InputStockDto inputStockDto);

}
