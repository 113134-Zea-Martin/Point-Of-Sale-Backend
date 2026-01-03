package com.scaffold.template.services.impl;

import com.scaffold.template.config.MovementStockMapper;
import com.scaffold.template.dtos.MovementStockCreateDto;
import com.scaffold.template.dtos.MovementStockResponseDto;
import com.scaffold.template.entities.MovementStockEntity;
import com.scaffold.template.entities.MovementType;
import com.scaffold.template.entities.ProductEntity;
import com.scaffold.template.entities.Reason;
import com.scaffold.template.entities.UserEntity;
import com.scaffold.template.repositories.MovementStockRepository;
import com.scaffold.template.repositories.ProductRepository;
import com.scaffold.template.repositories.UserRepository;
import com.scaffold.template.services.MovementStockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

    @Override
    public Page<MovementStockResponseDto> findMovementssByFilters(String productName,
                                                                  String movementType,
                                                                  String reason,
                                                                  Long userId,
                                                                  String fromDate,
                                                                  String toDate,
                                                                  Pageable pageable) {

        LocalDateTime fromDateTime = null;
        LocalDateTime toDateTime = null;

        try {
            if (fromDate != null && !fromDate.isBlank()) {
                // intenta parsear como LocalDate (yyyy-MM-dd)
                try {
                    fromDateTime = java.time.LocalDate.parse(fromDate).atStartOfDay();
                } catch (java.time.format.DateTimeParseException ex) {
                    // si no es solo fecha, intenta LocalDateTime ISO
                    fromDateTime = java.time.LocalDateTime.parse(fromDate);
                }
            }

            if (toDate != null && !toDate.isBlank()) {
                try {
                    // convertir toDate (yyyy-MM-dd) al final del día
                    toDateTime = java.time.LocalDate.parse(toDate)
                            .plusDays(1)
                            .atStartOfDay()
                            .minusNanos(1);
                } catch (java.time.format.DateTimeParseException ex) {
                    // si viene con hora, parsear directamente
                    toDateTime = java.time.LocalDateTime.parse(toDate);
                }
            }
        } catch (java.time.format.DateTimeParseException ex) {
            throw new IllegalArgumentException("Formato de fecha inválido. Usar 'yyyy-MM-dd' o ISO datetime 'yyyy-MM-ddTHH:mm:ss'", ex);
        }

        MovementType movementTypeEnum = null;
        if (movementType != null) {
            try {
                movementTypeEnum = MovementType.valueOf(movementType.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Tipo de movimiento inválido: " + movementType);
            }
        }

        Page<MovementStockEntity> movementStockEntities = movementStockRepository.findByFilters(
                productName,
                movementTypeEnum,
                reason != null ? Enum.valueOf(Reason.class, reason.toUpperCase()) : null,
                userId,
                fromDateTime,
                toDateTime,
                pageable
        );


        // mapear y calcular stockResultante por movimiento
        return movementStockEntities.map(entity -> {
            MovementStockResponseDto dto = movementStockMapper.toMovementStockResponseDto(entity);

            Long productId = entity.getProduct() != null ? entity.getProduct().getId() : null;
            LocalDateTime date = entity.getDate();

            if (productId != null && date != null) {
                // Ajustar MovementType.IN / MovementType.OUT según tu enum real
                Integer stock = movementStockRepository.getStockUntil(productId, date, MovementType.INPUT, MovementType.OUTPUT);
                dto.setStockResultante(stock == null ? 0 : stock);
            } else {
                dto.setStockResultante(0);
            }

            return dto;
        });    }
}
