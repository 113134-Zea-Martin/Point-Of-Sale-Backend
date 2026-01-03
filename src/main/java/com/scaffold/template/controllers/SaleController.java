package com.scaffold.template.controllers;

import com.scaffold.template.dtos.SaleCreateDto;
import com.scaffold.template.dtos.SaleResponseDto;
import com.scaffold.template.dtos.SaleResponseWithNameDto;
import com.scaffold.template.entities.PaymentType;
import com.scaffold.template.entities.SaleEntity;
import com.scaffold.template.services.SellService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

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

    @GetMapping("")
    public ResponseEntity<Page<SaleResponseWithNameDto>> getSalesByFilters(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) PaymentType paymentType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = false) Double minTotal,
            @RequestParam(required = false) Double maxTotal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String sort) {
        try {

            // convertir LocalDate a LocalDateTime (inicio y fin del día) para la consulta
            LocalDateTime fromDateTime = fromDate != null ? fromDate.atStartOfDay() : null;
            LocalDateTime toDateTime = toDate != null ? toDate.atTime(LocalTime.MAX) : null;

            String[] sortParts = sort.split(",");
            Sort.Direction direction = sortParts.length > 1 && "desc".equalsIgnoreCase(sortParts[1]) ? Sort.Direction.DESC : Sort.Direction.ASC;
            String sortBy = sortParts[0];
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
            Page<SaleResponseWithNameDto> salesPage = sellService.findSalesByFilters(
                    userId, paymentType != null ? paymentType.name() : null,
                    fromDate != null ? fromDateTime.toString() : null,
                    toDate != null ? toDateTime.toString() : null,
                    minTotal, maxTotal, pageable);
            if (salesPage.isEmpty()) {
                log.info("No se encontraron ventas con los filtros proporcionados");
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(salesPage);
        } catch (Exception e) {
            log.error("Error al obtener las ventas: {}", e.getMessage());
            throw e;
        }
    }
}