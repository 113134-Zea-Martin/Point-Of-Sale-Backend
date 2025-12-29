package com.scaffold.template.dtos;

import lombok.Data;

@Data
public class ProductResponseDTO {

    private Long id;
    private String barcode;
    private String name;
    private String brandName;
    private String categoryName;
    private Double purchasePrice;
    private Double salePrice;
    private Integer currentStock;
    private Integer minimumStock;
    private Boolean active;
}
