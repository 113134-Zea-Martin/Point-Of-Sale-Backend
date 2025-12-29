package com.scaffold.template.dtos;

import com.scaffold.template.entities.Reason;
import lombok.Data;

@Data
public class InputStockDto {
    private Long productId;
    private Integer quantity;
//    private Reason reason;
    private Long userId;
}
