// java
package com.scaffold.template.config;

import com.scaffold.template.entities.CashRegisterClosureEntity;
import com.scaffold.template.entities.PaymentType;
import com.scaffold.template.entities.RoleEntity;
import com.scaffold.template.entities.UserEntity;
import com.scaffold.template.repositories.CashRegisterClosureRepository;
import com.scaffold.template.repositories.RoleRepository;
import com.scaffold.template.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.scaffold.template.entities.BrandEntity;
import com.scaffold.template.entities.CategoryEntity;
import com.scaffold.template.entities.ProductEntity;
import com.scaffold.template.repositories.BrandRepository;
import com.scaffold.template.repositories.CategoryRepository;
import com.scaffold.template.repositories.ProductRepository;

import com.scaffold.template.entities.SaleEntity;
import com.scaffold.template.entities.SaleDetailEntity;
import com.scaffold.template.repositories.SaleRepository;

import com.scaffold.template.entities.MovementStockEntity;
import com.scaffold.template.entities.MovementType;
import com.scaffold.template.entities.Reason;
import com.scaffold.template.repositories.MovementStockRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

@Component
@ConditionalOnProperty(prefix = "app.dataloader", name = "enabled", havingValue = "true", matchIfMissing = false)
public class DataLoader implements CommandLineRunner {

    private final BrandRepository brandRepo;
    private final CategoryRepository categoryRepo;
    private final ProductRepository productRepo;
    private final RoleRepository roleRepo;
    private final UserRepository userRepo;
    private final SaleRepository saleRepo;
    private final MovementStockRepository movementRepo;
    private final CashRegisterClosureRepository cashRegisterClosureRepo;

    public DataLoader(BrandRepository brandRepo,
                      CategoryRepository categoryRepo,
                      ProductRepository productRepo,
                      RoleRepository roleRepo,
                      UserRepository userRepo,
                      SaleRepository saleRepo,
                      MovementStockRepository movementRepo,
                      CashRegisterClosureRepository cashRegisterClosureRepo) {
        this.brandRepo = brandRepo;
        this.categoryRepo = categoryRepo;
        this.productRepo = productRepo;
        this.roleRepo = roleRepo;
        this.userRepo = userRepo;
        this.saleRepo = saleRepo;
        this.movementRepo = movementRepo;
        this.cashRegisterClosureRepo = cashRegisterClosureRepo;
    }

    @Override
    public void run(String... args) throws Exception {
        if (productRepo.count() > 0) return; // evita duplicados

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setRoleName("ADMIN");
        roleRepo.save(roleEntity);

        UserEntity adminUser = new UserEntity();
        adminUser.setUsername("admin");
        adminUser.setPassword("admin");
        adminUser.setRole(roleEntity);
        adminUser.setActive(true);
        userRepo.save(adminUser);

       /* BrandEntity brandA = new BrandEntity();
        brandA.setName("Marca A");
        BrandEntity brandB = new BrandEntity();
        brandB.setName("Marca B");
        brandRepo.saveAll(Arrays.asList(brandA, brandB));

        CategoryEntity catElect = new CategoryEntity();
        catElect.setName("Electrónica");
        CategoryEntity catHome = new CategoryEntity();
        catHome.setName("Hogar");
        categoryRepo.saveAll(Arrays.asList(catElect, catHome));

        ProductEntity p1 = new ProductEntity();
        p1.setBarcode("000111222333");
        p1.setName("Auriculares Inalámbricos");
        p1.setBrand(brandA);
        p1.setCategory(catElect);
        p1.setPurchasePrice(20.0);
        p1.setSalePrice(35.0);
        p1.setCurrentStock(100);
        p1.setMinimumStock(5);
        p1.setActive(Boolean.TRUE);

        ProductEntity p2 = new ProductEntity();
        p2.setBarcode("000111222334");
        p2.setName("Altavoz Bluetooth");
        p2.setBrand(brandA);
        p2.setCategory(catElect);
        p2.setPurchasePrice(25.0);
        p2.setSalePrice(45.0);
        p2.setCurrentStock(60);
        p2.setMinimumStock(5);
        p2.setActive(Boolean.TRUE);

        ProductEntity p3 = new ProductEntity();
        p3.setBarcode("000111222335");
        p3.setName("Plancha de Vapor");
        p3.setBrand(brandB);
        p3.setCategory(catHome);
        p3.setPurchasePrice(18.0);
        p3.setSalePrice(30.0);
        p3.setCurrentStock(40);
        p3.setMinimumStock(3);
        p3.setActive(Boolean.TRUE);

        ProductEntity p4 = new ProductEntity();
        p4.setBarcode("000111222336");
        p4.setName("Lámpara LED");
        p4.setBrand(brandB);
        p4.setCategory(catHome);
        p4.setPurchasePrice(5.0);
        p4.setSalePrice(12.0);
        p4.setCurrentStock(150);
        p4.setMinimumStock(10);
        p4.setActive(Boolean.FALSE);

        ProductEntity p5 = new ProductEntity();
        p5.setBarcode("000111222337");
        p5.setName("Cargador USB-C");
        p5.setBrand(brandA);
        p5.setCategory(catElect);
        p5.setPurchasePrice(3.0);
        p5.setSalePrice(8.0);
        p5.setCurrentStock(200);
        p5.setMinimumStock(20);
        p5.setActive(Boolean.TRUE);

        productRepo.saveAll(Arrays.asList(p1, p2, p3, p4, p5));

        // --- Ventas de ejemplo existentes ---
        SaleEntity sale1 = new SaleEntity();
        sale1.setDateTime(LocalDateTime.now().minusDays(2));
        sale1.setUser(adminUser);
        sale1.setPaymentType(PaymentType.CASH);
        SaleDetailEntity sd1 = new SaleDetailEntity();
        sd1.setSale(sale1);
        sd1.setProduct(p1);
        sd1.setQuantity(2);
        sd1.setUnitPrice(p1.getSalePrice());
        sd1.setSubtotal(sd1.getQuantity() * sd1.getUnitPrice());

        SaleDetailEntity sd2 = new SaleDetailEntity();
        sd2.setSale(sale1);
        sd2.setProduct(p5);
        sd2.setQuantity(3);
        sd2.setUnitPrice(p5.getSalePrice());
        sd2.setSubtotal(sd2.getQuantity() * sd2.getUnitPrice());

        sale1.setSaleDetails(Arrays.asList(sd1, sd2));
        sale1.setTotal(sd1.getSubtotal() + sd2.getSubtotal());

        SaleEntity sale2 = new SaleEntity();
        sale2.setDateTime(LocalDateTime.now().minusDays(1));
        sale2.setUser(adminUser);
        sale2.setPaymentType(PaymentType.CREDIT_CARD);

        SaleDetailEntity sd3 = new SaleDetailEntity();
        sd3.setSale(sale2);
        sd3.setProduct(p2);
        sd3.setQuantity(1);
        sd3.setUnitPrice(p2.getSalePrice());
        sd3.setSubtotal(sd3.getQuantity() * sd3.getUnitPrice());

        SaleDetailEntity sd4 = new SaleDetailEntity();
        sd4.setSale(sale2);
        sd4.setProduct(p3);
        sd4.setQuantity(2);
        sd4.setUnitPrice(p3.getSalePrice());
        sd4.setSubtotal(sd4.getQuantity() * sd4.getUnitPrice());

        sale2.setSaleDetails(Arrays.asList(sd3, sd4));
        sale2.setTotal(sd3.getSubtotal() + sd4.getSubtotal());

        saleRepo.saveAll(Arrays.asList(sale1, sale2));

        // --- Movimientos de stock de ejemplo (varias fechas y tipos) ---
        MovementStockEntity m1 = new MovementStockEntity();
        m1.setProduct(p1);
        m1.setMovementType(MovementType.INPUT);
        m1.setReason(Reason.PURCHASE);
        m1.setQuantity(50);
        m1.setDate(LocalDateTime.now().minusDays(12));
        m1.setUser(adminUser);

        MovementStockEntity m2 = new MovementStockEntity();
        m2.setProduct(p2);
        m2.setMovementType(MovementType.INPUT);
        m2.setReason(Reason.PURCHASE);
        m2.setQuantity(30);
        m2.setDate(LocalDateTime.now().minusDays(9).minusHours(3));
        m2.setUser(adminUser);

        MovementStockEntity m3 = new MovementStockEntity();
        m3.setProduct(p1);
        m3.setMovementType(MovementType.OUTPUT);
        m3.setReason(Reason.SALE);
        m3.setQuantity(2);
        m3.setDate(LocalDateTime.now().minusDays(2).minusHours(1)); // cerca de sale1
        m3.setUser(adminUser);

        MovementStockEntity m4 = new MovementStockEntity();
        m4.setProduct(p5);
        m4.setMovementType(MovementType.OUTPUT);
        m4.setReason(Reason.SALE);
        m4.setQuantity(3);
        m4.setDate(LocalDateTime.now().minusDays(2).plusHours(2));
        m4.setUser(adminUser);

        MovementStockEntity m5 = new MovementStockEntity();
        m5.setProduct(p3);
        m5.setMovementType(MovementType.INPUT);
        m5.setReason(Reason.PURCHASE);
        m5.setQuantity(20);
        m5.setDate(LocalDateTime.now().minusDays(5).minusHours(6));
        m5.setUser(adminUser);

        MovementStockEntity m6 = new MovementStockEntity();
        m6.setProduct(p3);
        m6.setMovementType(MovementType.OUTPUT);
        m6.setReason(Reason.SALE);
        m6.setQuantity(2);
        m6.setDate(LocalDateTime.now().minusDays(1).minusHours(2)); // cerca de sale2
        m6.setUser(adminUser);

        MovementStockEntity m7 = new MovementStockEntity();
        m7.setProduct(p4);
        m7.setMovementType(MovementType.INPUT);
        m7.setReason(Reason.ADJUSTMENT);
        m7.setQuantity(15);
        m7.setDate(LocalDateTime.now().minusDays(20));
        m7.setUser(adminUser);

        MovementStockEntity m8 = new MovementStockEntity();
        m8.setProduct(p2);
        m8.setMovementType(MovementType.OUTPUT);
        m8.setReason(Reason.SALE);
        m8.setQuantity(1);
        m8.setDate(LocalDateTime.now().minusDays(1).plusHours(1));
        m8.setUser(adminUser);

        movementRepo.saveAll(Arrays.asList(m1, m2, m3, m4, m5, m6, m7, m8));

        // --- Crear cierres para hoy y los 7 días anteriores, generando ventas por día ---
        PaymentType[] paymentTypes = new PaymentType[] {
                PaymentType.CASH, PaymentType.DEBIT_CARD, PaymentType.CREDIT_CARD, PaymentType.MERCADO_PAGO
        };

        for (int i = 1; i <= 8; i++) {
            LocalDate date = LocalDate.now().minusDays(i);

            // Generar entre 1 y 3 ventas para la fecha (determinístico)
            List<SaleEntity> generatedSales = new ArrayList<>();
            int salesCount = 1 + (i % 3); // 1,2,3 repetitivo
            for (int j = 0; j < salesCount; j++) {
                LocalDateTime startOfDay = date.atStartOfDay();
                LocalDateTime saleTime = startOfDay.plusHours(9 + j * 3 + (i % 4)); // horario dentro del día

                SaleEntity sale = new SaleEntity();
                sale.setDateTime(saleTime);
                sale.setUser(adminUser);
                sale.setPaymentType(paymentTypes[(i + j) % paymentTypes.length]);

                List<SaleDetailEntity> details = new ArrayList<>();

                // primer detalle
                ProductEntity prod1 = switch (j % 5) {
                    case 0 -> p1;
                    case 1 -> p2;
                    case 2 -> p3;
                    case 3 -> p4;
                    default -> p5;
                };
                SaleDetailEntity d1 = new SaleDetailEntity();
                d1.setSale(sale);
                d1.setProduct(prod1);
                d1.setQuantity(1 + ((i + j) % 3));
                d1.setUnitPrice(prod1.getSalePrice());
                d1.setSubtotal(d1.getQuantity() * d1.getUnitPrice());
                details.add(d1);

                // opcional segundo detalle para variabilidad
                if ((i + j) % 2 == 0) {
                    ProductEntity prod2 = switch ((j + 1) % 5) {
                        case 0 -> p1;
                        case 1 -> p2;
                        case 2 -> p3;
                        case 3 -> p4;
                        default -> p5;
                    };
                    SaleDetailEntity d2 = new SaleDetailEntity();
                    d2.setSale(sale);
                    d2.setProduct(prod2);
                    d2.setQuantity(1 + ((i + j + 1) % 2));
                    d2.setUnitPrice(prod2.getSalePrice());
                    d2.setSubtotal(d2.getQuantity() * d2.getUnitPrice());
                    details.add(d2);
                }

                double total = 0;
                for (SaleDetailEntity sd : details) total += sd.getSubtotal();

                sale.setSaleDetails(details);
                sale.setTotal(total);

                generatedSales.add(sale);
            }

            // guardar ventas generadas para el día antes de calcular el cierre
            if (!generatedSales.isEmpty()) {
                saleRepo.saveAll(generatedSales);
            }

            if (cashRegisterClosureRepo.existsByDate(date)) continue;

            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(23, 59, 59);

            List<SaleEntity> salesOfDay = saleRepo.findByDateTimeBetween(startOfDay, endOfDay);

            double cash = 0;
            double debit = 0;
            double credit = 0;
            double mp = 0;

            for (SaleEntity sale : salesOfDay) {
                if (sale.getPaymentType() == null) continue;
                switch (sale.getPaymentType()) {
                    case CASH -> cash += sale.getTotal();
                    case DEBIT_CARD -> debit += sale.getTotal();
                    case CREDIT_CARD -> credit += sale.getTotal();
                    case MERCADO_PAGO -> mp += sale.getTotal();
                }
            }

            CashRegisterClosureEntity closure = new CashRegisterClosureEntity();
            closure.setDate(date);
            closure.setCashTotal(cash);
            closure.setDebitTotal(debit);
            closure.setCreditTotal(credit);
            closure.setMercadoPagoTotal(mp);
            closure.setTotalSales(cash + debit + credit + mp);
            closure.setTotalOperations(salesOfDay.size());
            // marcar closedAt cerca del final del día
            closure.setClosedAt(endOfDay);
            closure.setClosedBy(adminUser);

            cashRegisterClosureRepo.save(closure);*/
        }
    }

