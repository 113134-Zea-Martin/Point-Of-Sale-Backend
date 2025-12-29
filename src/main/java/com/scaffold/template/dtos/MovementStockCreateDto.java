package com.scaffold.template.dtos;

import com.scaffold.template.entities.MovementType;
import com.scaffold.template.entities.Reason;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MovementStockCreateDto {
    private Long productId;
    private MovementType movementType;
    private Reason reason;
    private Integer quantity;
    private LocalDateTime date;
    private Long userId;
}
