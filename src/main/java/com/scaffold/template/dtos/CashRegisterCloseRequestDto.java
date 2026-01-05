package com.scaffold.template.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CashRegisterCloseRequestDto {
    private LocalDate date; // generalmente hoy
    private Long userId;
}
