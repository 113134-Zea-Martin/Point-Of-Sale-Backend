package com.scaffold.template.services.impl;

import com.scaffold.template.config.SaleMapper;
import com.scaffold.template.dtos.SaleCreateDto;
import com.scaffold.template.dtos.SaleDetailDto;
import com.scaffold.template.dtos.SaleResponseDto;
import com.scaffold.template.entities.MovementStockEntity;
import com.scaffold.template.entities.MovementType;
import com.scaffold.template.entities.ProductEntity;
import com.scaffold.template.entities.Reason;
import com.scaffold.template.entities.SaleDetailEntity;
import com.scaffold.template.entities.SaleEntity;
import com.scaffold.template.repositories.MovementStockRepository;
import com.scaffold.template.repositories.ProductRepository;
import com.scaffold.template.repositories.SaleDetailRepository;
import com.scaffold.template.repositories.SaleRepository;
import com.scaffold.template.repositories.UserRepository;
import com.scaffold.template.services.SellService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
public class SellServiceImpl implements SellService {

    private final SaleRepository saleRepository;
    private final SaleDetailRepository saleDetailRepository;
    private final ProductRepository productRepository;
    private final MovementStockRepository movementStockRepository;
    private final UserRepository userRepository;
    private final SaleMapper saleMapper;

    public SellServiceImpl(SaleRepository saleRepository,
                           SaleDetailRepository saleDetailRepository,
                           ProductRepository productRepository,
                           MovementStockRepository movementStockRepository,
                           UserRepository userRepository, SaleMapper saleMapper) {
        this.saleRepository = saleRepository;
        this.saleDetailRepository = saleDetailRepository;
        this.productRepository = productRepository;
        this.movementStockRepository = movementStockRepository;
        this.userRepository = userRepository;
        this.saleMapper = saleMapper;
    }

    @Override
    @Transactional
    public SaleResponseDto save(SaleCreateDto saleCreateDto) {
        log.info("Iniciando proceso de venta para el usuario ID: {}", saleCreateDto.getUserId());

        // Validar carrito no vacío
        validateCart(saleCreateDto);

        // Crear Venta
        SaleEntity sale = createSale(saleCreateDto);

        double calculatedTotal = 0.0;

        // Procesar cada detalle de venta
        for (SaleDetailDto detailDto : saleCreateDto.getSaleDetails()) {
            log.info("Procesando detalle de venta para el producto con codigo de barra: {}", detailDto.getBarcode());
            calculatedTotal += processSaleDetail(detailDto, sale);
        }

        // Validar total calculado con total proporcionado
        if (Double.compare(calculatedTotal, saleCreateDto.getTotal()) != 0) {
            throw new IllegalArgumentException("El total de la venta no coincide con la suma de los detalles.");
        }

        log.info("Venta procesada exitosamente con ID: {}", sale.getId());
        return saleMapper.map(sale);
    }

    //Validar carrito no vacío
    public void validateCart(SaleCreateDto saleCreateDto) {
        if (saleCreateDto.getSaleDetails() == null || saleCreateDto.getSaleDetails().isEmpty()) {
            throw new IllegalArgumentException("La compra no puede estar vacía.");
        }
    }

    //Crear Venta
    public SaleEntity createSale(SaleCreateDto saleCreateDto) {
        SaleEntity saleEntity = new SaleEntity();
        saleEntity.setDateTime(LocalDateTime.now());
        saleEntity.setTotal(saleCreateDto.getTotal());
        saleEntity.setPaymentType(saleCreateDto.getPaymentType());
        saleEntity.setUser(userRepository.findById(saleCreateDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado.")));
        saleEntity.setSaleDetails(new ArrayList<>());
        return saleRepository.save(saleEntity);
    }

    public double processSaleDetail(SaleDetailDto saleDetailDto, SaleEntity sale) {

        // 1. Buscar producto
        ProductEntity product = productRepository.findByBarcode(saleDetailDto.getBarcode())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Producto no encontrado con código de barra: " + saleDetailDto.getBarcode()
                ));

        // 2. Validar stock
        if (product.getCurrentStock() < saleDetailDto.getQuantity()) {
            throw new IllegalArgumentException(
                    "Stock insuficiente para el producto: " + product.getName()
            );
        }

        // 3. Descontar stock
        product.setCurrentStock(product.getCurrentStock() - saleDetailDto.getQuantity());
        productRepository.save(product);

        // 4. Crear detalle de venta
        SaleDetailEntity detail = new SaleDetailEntity();
        detail.setSale(sale);
        detail.setProduct(product);
        detail.setQuantity(saleDetailDto.getQuantity());
        detail.setUnitPrice(product.getSalePrice());

        double subtotal = product.getSalePrice() * saleDetailDto.getQuantity();
        detail.setSubtotal(subtotal);

        SaleDetailEntity savedDetail = saleDetailRepository.save(detail);

        // Añadir el detalle guardado a la venta en memoria (evita nulls al mapear/serializar)
        if (sale.getSaleDetails() == null) {
            sale.setSaleDetails(new ArrayList<>());
        }
        sale.getSaleDetails().add(savedDetail);

        // 5. Crear movimiento de stock (SALIDA)
        MovementStockEntity movement = new MovementStockEntity();
        movement.setProduct(product);
        movement.setQuantity(saleDetailDto.getQuantity());
        movement.setMovementType(MovementType.OUTPUT);
        movement.setReason(Reason.SALE);
        movement.setDate(LocalDateTime.now());
        movement.setUser(sale.getUser());

        movementStockRepository.save(movement);

        return subtotal;
    }

}
