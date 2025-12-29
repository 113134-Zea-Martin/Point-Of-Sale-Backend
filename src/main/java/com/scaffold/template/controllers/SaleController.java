package com.scaffold.template.controllers;

import com.scaffold.template.dtos.SaleCreateDto;
import com.scaffold.template.dtos.SaleResponseDto;
import com.scaffold.template.entities.SaleEntity;
import com.scaffold.template.services.SellService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/sales")
@Tag(name = "Ventas", description = "API para gestión de ventas")
public class SaleController {

    private final SellService sellService;

    public SaleController(SellService sellService) {
        this.sellService = sellService;
    }

    @PostMapping("")
    public ResponseEntity<SaleResponseDto> createSale(@RequestBody SaleCreateDto saleCreateDto) {
        log.info("Recibiendo solicitud para crear una nueva venta");
        SaleResponseDto saleResponseDto = sellService.save(saleCreateDto);
        log.info("Venta creada exitosamente para el usuario ID: {}", saleCreateDto.getUserId());
        return ResponseEntity.ok(saleResponseDto);
    }

}
