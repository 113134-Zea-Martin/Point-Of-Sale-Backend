package com.scaffold.template.controllers;

import com.scaffold.template.dtos.CashRegisterCloseRequestDto;
import com.scaffold.template.dtos.CashRegisterClosureResponseDto;
import com.scaffold.template.services.CashRegisterClosureService;
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

import java.time.LocalDate;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/cash-register-closures")
@Tag(name = "Cierres de Caja", description = "API para gestión de cierres de caja")
public class CashRegisterClosureController {

    private final CashRegisterClosureService cashRegisterClosureService;

    public CashRegisterClosureController(CashRegisterClosureService cashRegisterClosureService) {
        this.cashRegisterClosureService = cashRegisterClosureService;
    }

    @PostMapping("")
    public ResponseEntity<CashRegisterClosureResponseDto> closeCashRegister(@RequestBody CashRegisterCloseRequestDto requestDto) {
        try {
            CashRegisterClosureResponseDto responseDto = cashRegisterClosureService.closeCashRegister(requestDto.getDate(), requestDto.getUserId());
            return ResponseEntity.status(201).body(responseDto);
        } catch (IllegalArgumentException | IllegalStateException e) {
            log.error("Error al cerrar la caja: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error inesperado al cerrar la caja: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("")
    public ResponseEntity<Page<CashRegisterClosureResponseDto>> getCashRegisterClosure(
            @RequestParam (required = false) String fromDate,
            @RequestParam (required = false) String toDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String sort) {
        log.info("Recibiendo solicitud para obtener cierres de caja con filtros");
        try {
            String[] sortParts = sort.split(",");
            Sort.Direction direction = sortParts.length > 1 && "desc".equalsIgnoreCase(sortParts[1]) ? Sort.Direction.DESC : Sort.Direction.ASC;
            String sortBy = sortParts[0];
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

            LocalDate from = (fromDate != null) ? LocalDate.parse(fromDate) : null;
            LocalDate to = (toDate != null) ? LocalDate.parse(toDate) : null;

            Page<CashRegisterClosureResponseDto> responseDtos = cashRegisterClosureService.getCashRegisterClosures(from, to, pageable);
            return ResponseEntity.ok(responseDtos);
        } catch (Exception e) {
            log.error("Error inesperado al obtener los cierres de caja: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

}
