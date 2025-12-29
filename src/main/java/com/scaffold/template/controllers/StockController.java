package com.scaffold.template.controllers;

import com.scaffold.template.dtos.InputStockDto;
import com.scaffold.template.dtos.MovementStockCreateDto;
import com.scaffold.template.dtos.MovementStockResponseDto;
import com.scaffold.template.services.StockService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/stock")
@Tag(name = "Stock", description = "API para gestión de stock de productos")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping("/input")
    public ResponseEntity<MovementStockResponseDto> stockIn(@RequestBody InputStockDto inputStockDto) {
        log.info("Recibida solicitud para ingresar stock: {}", inputStockDto);
        MovementStockResponseDto responseDto = stockService.stockIn(inputStockDto);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/adjustment")
    public ResponseEntity<MovementStockResponseDto> stockOut(@RequestBody InputStockDto inputStockDto) {
        log.info("Recibida solicitud para ajustar stock: {}", inputStockDto);
        MovementStockResponseDto responseDto = stockService.stockOut(inputStockDto);
        return ResponseEntity.ok(responseDto);
    }

}
