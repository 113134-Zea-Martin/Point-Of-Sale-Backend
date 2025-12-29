package com.scaffold.template.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductUpdateDTO {

    @NotBlank
    @Size(min = 1, max = 100)
    private String barcode;

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotNull
    private Long brandId;

    @NotNull
    private Long categoryId;

    @NotNull
    @Positive
    private Double purchasePrice;

    @NotNull
    @Positive
    private Double salePrice;

    @NotNull
    @Min(0)
    private Integer currentStock;

    @NotNull
    @Min(0)
    private Integer minimumStock;

    @NotNull
    private Boolean active;
}
