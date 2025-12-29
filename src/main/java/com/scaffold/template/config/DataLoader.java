// java
package com.scaffold.template.config;

import com.scaffold.template.entities.RoleEntity;
import com.scaffold.template.entities.UserEntity;
import com.scaffold.template.repositories.RoleRepository;
import com.scaffold.template.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.scaffold.template.entities.BrandEntity;
import com.scaffold.template.entities.CategoryEntity;
import com.scaffold.template.entities.ProductEntity;
import com.scaffold.template.repositories.BrandRepository;
import com.scaffold.template.repositories.CategoryRepository;
import com.scaffold.template.repositories.ProductRepository;

import java.util.Arrays;

@Component
public class DataLoader implements CommandLineRunner {

    private final BrandRepository brandRepo;
    private final CategoryRepository categoryRepo;
    private final ProductRepository productRepo;
    private final RoleRepository roleRepo;
    private final UserRepository userRepo;

    public DataLoader(BrandRepository brandRepo,
                      CategoryRepository categoryRepo,
                      ProductRepository productRepo, RoleRepository roleRepo, UserRepository userRepo) {
        this.brandRepo = brandRepo;
        this.categoryRepo = categoryRepo;
        this.productRepo = productRepo;
        this.roleRepo = roleRepo;
        this.userRepo = userRepo;
    }

    @Override
    public void run(String... args) throws Exception {
        if (productRepo.count() > 0) return; // evita duplicados

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setRoleName("ADMIN");
        roleRepo.save(roleEntity);

        UserEntity adminUser = new UserEntity();
        adminUser.setUsername("admin");
        adminUser.setPassword("adminpass");
        adminUser.setRole(roleEntity);
        adminUser.setActive(true);
        userRepo.save(adminUser);

        BrandEntity brandA = new BrandEntity();
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
    }
}
