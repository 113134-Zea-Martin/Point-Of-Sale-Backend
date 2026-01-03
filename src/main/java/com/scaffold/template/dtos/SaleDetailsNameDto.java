package com.scaffold.template.dtos;

import lombok.Data;

@Data
public class SaleDetailsNameDto {
    String barcode;
    String name;
    Integer quantity;
    Double salePrice;
    Integer stockResultant;
}
