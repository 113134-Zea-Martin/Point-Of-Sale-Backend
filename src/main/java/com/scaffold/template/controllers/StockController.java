package com.scaffold.template.controllers;

import com.scaffold.template.dtos.InputStockDto;
import com.scaffold.template.dtos.MovementStockResponseDto;
import com.scaffold.template.services.MovementStockService;
import com.scaffold.template.services.StockService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/stock")
@Tag(name = "Stock", description = "API para gestión de stock de productos")
public class StockController {

    private final StockService stockService;
    private final MovementStockService movementStockService;

    public StockController(StockService stockService, MovementStockService movementStockService) {
        this.stockService = stockService;
        this.movementStockService = movementStockService;
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

    @GetMapping("")
    public ResponseEntity<Page<MovementStockResponseDto>> getStockMovementsByFilters(
            @RequestParam (required = false) String productName,
            @RequestParam (required = false) String movementType,
            @RequestParam (required = false) String reason,
            @RequestParam (required = false) String fromDate,
            @RequestParam (required = false) String toDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String sort) {
        log.info("Recibiendo solicitud para obtener movimientos de stock con filtros");
        try {
            String[] sortParts = sort.split(",");
            Sort.Direction direction = sortParts.length > 1 && "desc".equalsIgnoreCase(sortParts[1]) ? Sort.Direction.DESC : Sort.Direction.ASC;
            String sortBy = sortParts[0];
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

            Page<MovementStockResponseDto> movementsPage = movementStockService.findMovementssByFilters(
                    productName,
                    movementType,
                    reason,
                    1L, // Filtro de userId fijo a 1L por ahora
                    fromDate,
                    toDate,
                    pageable
            );
            if (movementsPage.isEmpty()) {
                log.info("No se encontraron movimientos de stock con los filtros proporcionados");
            }
            return ResponseEntity.ok(movementsPage);
        } catch (Exception e) {
            log.error("Error al obtener movimientos de stock con filtros", e);
            return ResponseEntity.status(500).build();
        }
    }

}
