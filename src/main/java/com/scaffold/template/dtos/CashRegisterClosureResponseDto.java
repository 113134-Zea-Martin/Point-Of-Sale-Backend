package com.scaffold.template.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class CashRegisterClosureResponseDto {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;

    private Double totalSales;
    private Double cashTotal;
    private Double debitTotal;
    private Double creditTotal;
    private Double mercadoPagoTotal;

    private Integer totalOperations;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime closedAt;
    private String closedBy;
}