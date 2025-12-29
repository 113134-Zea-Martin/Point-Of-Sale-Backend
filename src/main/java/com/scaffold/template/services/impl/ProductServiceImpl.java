package com.scaffold.template.services.impl;

import com.scaffold.template.config.ProductMapper.ProductMapper;
import com.scaffold.template.dtos.ProductCreateDTO;
import com.scaffold.template.dtos.ProductResponseDTO;
import com.scaffold.template.dtos.ProductUpdateDTO;
import com.scaffold.template.entities.BrandEntity;
import com.scaffold.template.entities.CategoryEntity;
import com.scaffold.template.entities.ProductEntity;
import com.scaffold.template.repositories.ProductRepository;
import com.scaffold.template.services.BrandService;
import com.scaffold.template.services.CategoryService;
import com.scaffold.template.services.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private  final CategoryService categoryService;
    private final BrandService brandService;

    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper, CategoryService categoryService, BrandService brandService) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.categoryService = categoryService;
        this.brandService = brandService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> findAllProducts() {
        log.debug("Buscando todos los productos");
        try {
            List<ProductEntity> products = productRepository.findByActiveTrue();
            if (products.isEmpty()) {
                log.info("No se encontraron productos");
                return List.of();
            }

            log.debug("Mapeando {} productos a DTOs", products.size());

            return products.stream()
                    .map(productMapper::toResponse)
                    .toList();

        } catch (Exception e) {
            log.error("Error al buscar los productos: {}", e.getMessage());
            throw new RuntimeException("No se pudieron obtener los productos", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> findProducts(String barcode, String name, String brand, String category, Boolean active, Pageable pageable) {
        log.debug("Buscando productos con filtros - barcode: {}, name: {}, brand: {}, category: {}, active: {}, pageable: {}", barcode, name, brand, category, active, pageable);
        try {
            Page<ProductEntity> page = productRepository.findByFilters(
                    (barcode == null || barcode.isBlank()) ? null : barcode,
                    (name == null || name.isBlank()) ? null : name,
                    (brand == null || brand.isBlank()) ? null : brand,
                    (category == null || category.isBlank()) ? null : category,
                    active,
                    pageable
            );
            return page.map(productMapper::toResponse);
        } catch (Exception e) {
            log.error("Error al buscar productos con filtros: {}", e.getMessage());
            throw new RuntimeException("No se pudieron obtener los productos", e);
        }
    }


    @Override
    @Transactional(readOnly = true)
    public ProductResponseDTO findProductByBarcode(String barcode) {
        log.debug("Buscando producto con código de barras: {}", barcode);

        if (barcode == null || barcode.isBlank()) {
            log.warn("Código de barras proporcionado es nulo o vacío");
            throw new IllegalArgumentException("El código de barras es requerido");
        }

        try {
            ProductEntity product = productRepository.findByBarcode(barcode)
                    .orElseThrow(() -> {
                        log.info("Producto con código de barras {} no encontrado", barcode);
                        return new RuntimeException("Producto no encontrado");
                    });
            log.debug("Mapeando producto a DTO");
            return productMapper.toResponse(product);
        } catch (Exception e) {
            log.error("Error al buscar el producto con código de barras {}: {}", barcode, e.getMessage());
            throw new RuntimeException("No se pudo obtener el producto", e);
        }
    }

    @Override
    public ProductResponseDTO createProduct(ProductCreateDTO productCreateRequestDTO) {

        if (productCreateRequestDTO == null) {
            log.warn("Solicitud de creación de producto es nula");
            throw new IllegalArgumentException("La solicitud de creación de producto no puede ser nula");
        }

        if(productRepository.existsByBarcode(productCreateRequestDTO.getBarcode())) {
            log.warn("El código de barras {} ya existe", productCreateRequestDTO.getBarcode());
            throw new IllegalArgumentException("El código de barras ya existe");
        }

        log.debug("Creando nuevo producto: {}", productCreateRequestDTO);


        BrandEntity brand = brandService.getBrandById(productCreateRequestDTO.getBrandId());

        CategoryEntity category = categoryService.getCategoryById(productCreateRequestDTO.getCategoryId());

        try {
            ProductEntity productEntity = productMapper.toEntity(productCreateRequestDTO, brand, category);

            ProductEntity savedProduct = productRepository.save(productEntity);
            log.debug("Producto creado exitosamente con ID: {}", savedProduct.getId());
            return productMapper.toResponse(savedProduct);
        } catch (Exception e) {
            log.error("Error al crear el producto: {}", e.getMessage());
            throw new RuntimeException("No se pudo crear el producto", e);
        }
    }

    @Override
    public ProductResponseDTO updateProduct(Long id, ProductUpdateDTO productUpdateRequestDTO) {
        if (productUpdateRequestDTO == null) {
            log.warn("Solicitud de actualización de producto es nula");
            throw new IllegalArgumentException("La solicitud de actualización de producto no puede ser nula");
        }
        log.debug("Actualizando producto con ID {}: {}", id, productUpdateRequestDTO);
        ProductEntity existingProduct = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.info("Producto con ID {} no encontrado para actualización", id);
                    return new IllegalArgumentException("Producto no encontrado");
                });

        BrandEntity brand = brandService.getBrandById(productUpdateRequestDTO.getBrandId());
        CategoryEntity category = categoryService.getCategoryById(productUpdateRequestDTO.getCategoryId());

        try {
            productMapper.updateEntity(productUpdateRequestDTO, existingProduct, brand, category);
            ProductEntity updatedProduct = productRepository.save(existingProduct);
            log.debug("Producto con ID {} actualizado exitosamente", id);
            return productMapper.toResponse(updatedProduct);
        } catch (Exception e) {
            log.error("Error al actualizar el producto con ID {}: {}", id, e.getMessage());
            throw new RuntimeException("No se pudo actualizar el producto", e);
        }
    }

//    @Override
//    @Transactional(readOnly = true)
//    public Page<ProductResponseDTO> findProducts(String name, String brand, String category, Boolean active, Pageable pageable) {
//        log.debug("Buscando productos con filtros - name: {}, brand: {}, category: {}, active: {}, pageable: {}", name, brand, category, active, pageable);
//        try {
//            Page<ProductEntity> page = productRepository.findByFilters(
//                    (name == null || name.isBlank()) ? null : name,
//                    (brand == null || brand.isBlank()) ? null : brand,
//                    (category == null || category.isBlank()) ? null : category,
//                    active,
//                    pageable
//            );
//            return page.map(productMapper::toResponse);
//        } catch (Exception e) {
//            log.error("Error al buscar productos con filtros: {}", e.getMessage());
//            throw new RuntimeException("No se pudieron obtener los productos", e);
//        }
//    }
}
