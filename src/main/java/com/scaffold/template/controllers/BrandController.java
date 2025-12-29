package com.scaffold.template.controllers;

import com.scaffold.template.dtos.BrandDto;
import com.scaffold.template.dtos.CreateBrandRequest;
import com.scaffold.template.services.BrandService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/brands")
@Tag(name = "Marcas", description = "API para gestión de marcas")
public class BrandController {

    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    // Método para obtener todas las marcas
    @GetMapping("")
    public ResponseEntity<List<BrandDto>> getAllBrands() {
        try {
            List<BrandDto> brands = brandService.getAllBrands();
            if (brands.isEmpty()) {
                log.info("No se encontraron marcas");
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(brands);
        } catch (Exception e) {
            log.error("Error al obtener las marcas: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    // Método para crear una nueva marca
    @PostMapping("")
    public ResponseEntity<BrandDto> createBrand(@RequestBody CreateBrandRequest createBrandRequest) {
        try {
            BrandDto newBrand = brandService.createBrand(createBrandRequest);
            return ResponseEntity.status(201).body(newBrand);
        } catch (Exception e) {
            log.error("Error al crear la marca: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }
}