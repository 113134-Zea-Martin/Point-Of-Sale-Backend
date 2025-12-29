package com.scaffold.template.services.impl;

import com.scaffold.template.config.MovementStockMapper;
import com.scaffold.template.config.ProductMapper.ProductMapper;
import com.scaffold.template.dtos.InputStockDto;
import com.scaffold.template.dtos.MovementStockCreateDto;
import com.scaffold.template.dtos.MovementStockResponseDto;
import com.scaffold.template.dtos.ProductResponseDTO;
import com.scaffold.template.dtos.ProductUpdateDTO;
import com.scaffold.template.entities.MovementStockEntity;
import com.scaffold.template.entities.MovementType;
import com.scaffold.template.entities.ProductEntity;
import com.scaffold.template.entities.Reason;
import com.scaffold.template.repositories.ProductRepository;
import com.scaffold.template.services.MovementStockService;
import com.scaffold.template.services.ProductService;
import com.scaffold.template.services.StockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@Transactional
public class StockServiceImpl implements StockService {

    private final MovementStockService movementStockService;
    private final MovementStockMapper movementStockMapper;
    private final ProductRepository productRepository;
    private final ProductService productService;
    private final ProductMapper productMapper;

    public StockServiceImpl(MovementStockService movementStockService,
                            MovementStockMapper movementStockMapper,
                            ProductRepository productRepository, ProductService productService, ProductMapper productMapper) {
        this.movementStockService = movementStockService;
        this.movementStockMapper = movementStockMapper;
        this.productRepository = productRepository;
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @Override
    @Transactional
    public MovementStockResponseDto stockIn(InputStockDto inputStockDto) {
        log.info("Iniciando ingreso de stock para producto ID: {}", inputStockDto.getProductId());

        try {
            // Validar entrada de datos
            validateInput(inputStockDto);

            // Obtener el producto
            ProductEntity product = getProduct(inputStockDto.getProductId());

            //Validar operacion
            validateStockOperation(product, inputStockDto.getQuantity());

            // Crear y registrar el movimiento de stock
            MovementStockEntity movementStockEntity = createAndRecordMovement(inputStockDto, product);

            // Actualizar el stock del producto
            updateProductStock(product, inputStockDto.getQuantity());

            // Log y retorno
            log.info("Ingreso de stock completado para producto ID: {}", inputStockDto.getProductId());
            return movementStockMapper.toMovementStockResponseDto(movementStockEntity);
        } catch (Exception e) {
            log.error("Error durante el ingreso de stock para producto ID: {}: {}",
                    inputStockDto.getProductId(), e.getMessage());
            throw e;
        }        
    }

    @Override
    public MovementStockResponseDto stockOut(InputStockDto inputStockDto) {
        log.info("Iniciando egreso de stock para producto ID: {}", inputStockDto.getProductId());

        try {
            // Validar entrada de datos
            validateInput(inputStockDto);

            // Obtener el producto
            ProductEntity product = getProduct(inputStockDto.getProductId());

            // Obtener la cantidad
            int quantity = inputStockDto.getQuantity();

            // Validar que hay stock suficiente
            if (product.getCurrentStock() < quantity) {
                throw new IllegalArgumentException("Stock insuficiente para el producto con ID: "
                        + inputStockDto.getProductId());
            }

            MovementStockEntity movementStockEntity = createAndRecordMovement(inputStockDto, product, MovementType.OUTPUT);

            // Actualizar el stock del producto
            updateProductStock(product, -quantity);

            // Log y retorno
            log.info("Salida de {} unidades de stock completada para producto: {}",
                    quantity, product.getName());

            return movementStockMapper.toMovementStockResponseDto(movementStockEntity);

        } catch (Exception e) {
            log.error("Error durante el egreso de stock para producto ID: {}: {}",
                    inputStockDto.getProductId(), e.getMessage());
            throw e;
        }
    }


    public void updateProductStock(ProductEntity product, Integer quantity) {
        int newStock = product.getCurrentStock() + quantity;
        if (newStock < 0) {
            throw new IllegalArgumentException("No se puede actualizar el stock. Stock insuficiente para el producto con ID: "
                    + product.getId());
        }
        product.setCurrentStock(newStock);
        if (newStock < product.getMinimumStock()) {
            log.warn("El stock del producto con ID: {} ha caído por debajo del stock mínimo. Stock actual: {}, Stock mínimo: {}",
                    product.getId(), newStock, product.getMinimumStock());
        }
        productRepository.save(product);
    }

    public  MovementStockEntity createAndRecordMovement(InputStockDto inputStockDto, ProductEntity product) {
        return createAndRecordMovement(inputStockDto, product, MovementType.INPUT);
    }

    public  MovementStockEntity createAndRecordMovement(InputStockDto inputStockDto,
                                                        ProductEntity product,
                                                        MovementType movementType) {
        MovementStockCreateDto movementStockCreateDto = new MovementStockCreateDto();
        movementStockCreateDto.setProductId(inputStockDto.getProductId());
        movementStockCreateDto.setMovementType(movementType);

        if (movementType == MovementType.INPUT) {
            movementStockCreateDto.setReason(Reason.PURCHASE);
        } else {
            movementStockCreateDto.setReason(Reason.ADJUSTMENT);
        }

        movementStockCreateDto.setQuantity(inputStockDto.getQuantity());
        movementStockCreateDto.setDate(LocalDateTime.now());
        movementStockCreateDto.setUserId(inputStockDto.getUserId());
        log.info("Registrando movimiento de stock: {}", movementStockCreateDto);
        return movementStockService.recordMovement(movementStockCreateDto);
    }

    public void validateStockOperation(ProductEntity product, Integer quantity) {
        if (!product.getActive()) {
            throw new IllegalArgumentException("No se puede realizar la operación. El producto: "
                    + product.getName() + " está inactivo.");
        }
    }

    public void validateInput(InputStockDto inputStockDto) {
        if (inputStockDto == null) {
            throw new IllegalArgumentException("La solicitud de ingreso de stock no puede ser nula");
        }
        if (inputStockDto.getProductId() == null) {
            throw new IllegalArgumentException("El ID del producto no puede ser nulo");
        }
        if (inputStockDto.getQuantity() == 0) {
            throw new IllegalArgumentException("La cantidad debe ser distinta de cero");
        }
//        if (inputStockDto.getReason() == null) {
//            throw new IllegalArgumentException("La razón del movimiento no puede ser nula");
//        }
        if (inputStockDto.getUserId() == null) {
            throw new IllegalArgumentException("El ID de usuario no puede ser nulo");
        }
    }

    public ProductEntity getProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + productId));
    }
}
