package com.scaffold.template.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.scaffold.template.entities.MovementType;
import com.scaffold.template.entities.Reason;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class MovementStockResponseDto {
    private Long id;
    private Long productId;
    private String productName;
    private MovementType movementType;
    private Reason reason;
    private Integer quantity;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;
    private Long userId;
}
