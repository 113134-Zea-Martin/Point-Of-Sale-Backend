package com.scaffold.template.dtos;

import com.scaffold.template.entities.PaymentType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SaleCreateDto {

    Double total;
    PaymentType paymentType;
    Long userId;
    List<SaleDetailDto> saleDetails;
}
