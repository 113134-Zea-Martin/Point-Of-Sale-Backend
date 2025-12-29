package com.scaffold.template.controllers;

import com.scaffold.template.dtos.ProductCreateDTO;
import com.scaffold.template.dtos.ProductResponseDTO;
import com.scaffold.template.dtos.ProductUpdateDTO;
import com.scaffold.template.services.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Productos", description = "API para gestión de productos")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("")
    public ResponseEntity<Page<ProductResponseDTO>> getAllProducts(
            @RequestParam(required = false) String barcode,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Boolean active,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String sort
    ) {
        try {
            String[] sortParts = sort.split(",");
            Sort.Direction direction = sortParts.length > 1 && "desc".equalsIgnoreCase(sortParts[1]) ? Sort.Direction.DESC : Sort.Direction.ASC;
            String sortBy = sortParts[0];
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

            Page<ProductResponseDTO> productsPage = productService.findProducts(barcode, name, brand, category, active, pageable);
            if (productsPage.isEmpty()) {
                log.info("No se encontraron productos");
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(productsPage);
        } catch (Exception e) {
            log.error("Error al obtener los productos: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/{codigoBarras}")
    public ResponseEntity<ProductResponseDTO> getProductByBarcode(@PathVariable String codigoBarras) {
        try {
            log.debug("Recibiendo solicitud para obtener producto con código de barras: {}", codigoBarras);
            ProductResponseDTO product = productService.findProductByBarcode(codigoBarras);
            if (product == null) {
                log.info("Producto no encontrado para barcode: {}", codigoBarras);
                return ResponseEntity.notFound().build();
            }
            log.debug("Producto obtenido exitosamente: {}", product);
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            log.error("Error al obtener el producto: {}", e.getMessage());
            throw e;
        }
    }

    @PostMapping("")
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody ProductCreateDTO dto) {
        try {
            log.debug("Recibiendo solicitud para crear un nuevo producto: {}", dto);
            ProductResponseDTO createdProduct = productService.createProduct(dto);
            log.debug("Producto creado exitosamente: {}", createdProduct);
            return ResponseEntity.ok(createdProduct);
        } catch (Exception e) {
            log.error("Error al crear el producto: {}", e.getMessage());
            throw e;
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable Long id, @RequestBody ProductUpdateDTO dto) {
        try {
            log.debug("Recibiendo solicitud para actualizar el producto con id {}: {}", id, dto);
            ProductResponseDTO updatedProduct = productService.updateProduct(id, dto);

            log.debug("Producto actualizado exitosamente: {}", updatedProduct);
            return ResponseEntity.ok(updatedProduct);
        } catch (Exception e) {
            log.error("Error al actualizar el producto: {}", e.getMessage());
            throw e;
        }
    }

}
