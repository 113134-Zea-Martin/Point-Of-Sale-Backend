package com.scaffold.template.services.impl;

import com.scaffold.template.config.MovementStockMapper;
import com.scaffold.template.dtos.MovementStockCreateDto;
import com.scaffold.template.entities.MovementStockEntity;
import com.scaffold.template.entities.MovementType;
import com.scaffold.template.entities.ProductEntity;
import com.scaffold.template.entities.UserEntity;
import com.scaffold.template.repositories.MovementStockRepository;
import com.scaffold.template.repositories.ProductRepository;
import com.scaffold.template.repositories.UserRepository;
import com.scaffold.template.services.MovementStockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MovementStockServiceImpl implements MovementStockService {

    private final MovementStockRepository movementStockRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final MovementStockMapper movementStockMapper;

    public MovementStockServiceImpl(MovementStockRepository movementStockRepository,
                                    ProductRepository productRepository,
                                    UserRepository userRepository,
                                    MovementStockMapper movementStockMapper) {
        this.movementStockRepository = movementStockRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.movementStockMapper = movementStockMapper;
    }

    @Override
    public MovementStockEntity recordMovement(MovementStockCreateDto movementStockCreateDto) {

        log.info("Movimiento de stock recibido: {}", movementStockCreateDto);

        if(movementStockCreateDto.getProductId() == null) {
            throw new IllegalArgumentException("El ID del producto no puede ser nulo");
        }
        if (movementStockCreateDto.getMovementType() == null) {
            throw new IllegalArgumentException("El tipo de movimiento no puede ser nulo");
        }
        if (movementStockCreateDto.getReason() == null) {
            throw new IllegalArgumentException("La razón del movimiento no puede ser nula");
        }

        if (movementStockCreateDto.getQuantity() == 0) {
            throw new IllegalArgumentException("Cantidad debe ser distinta de cero");
        }

        if (movementStockCreateDto.getDate() == null) {
            throw new IllegalArgumentException("La fecha no puede ser nula");
        }

        if (movementStockCreateDto.getUserId() == null) {
            throw new IllegalArgumentException("El ID de usuario no puede ser nulo");
        }

        ProductEntity productEntity = productRepository.findById(movementStockCreateDto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: "
                        + movementStockCreateDto.getProductId()));

        log.info("Validamos el stock para el movimiento: {}", movementStockCreateDto);
        // Validar si la cantidad resulta en stock es negativa, no permitir la salida
        if (movementStockCreateDto.getMovementType() == MovementType.OUTPUT) {
            int projectedStock = productEntity.getCurrentStock() + movementStockCreateDto.getQuantity();
            if (projectedStock < 0) {
                throw new IllegalArgumentException("No se puede realizar la salida. Stock insuficiente para el producto con ID: "
                        + movementStockCreateDto.getProductId());
            }
        }

        UserEntity userEntity = userRepository.findById(movementStockCreateDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: "
                        + movementStockCreateDto.getUserId()));

        MovementStockEntity movementStockEntity = movementStockMapper.toEntity(movementStockCreateDto,
                productEntity,
                userEntity);
        MovementStockEntity savedMovement = movementStockRepository.save(movementStockEntity);
        log.info("Movimiento de stock registrado exitosamente: {}", savedMovement);
        return savedMovement;
    }
}
