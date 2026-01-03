package com.scaffold.template.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SaleResponseWithNameDto {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateTime;
    private Double total;
    private String paymentType;
    private Long userId;
    private List<SaleDetailsNameDto> saleDetailsNameDtoList;
}
