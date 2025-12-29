package com.scaffold.template.repositories;

import com.scaffold.template.entities.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    // Obtener todos los productos activos
    List<ProductEntity> findByActiveTrue();
    Optional<ProductEntity> findByBarcode(String barcode);

    Boolean existsByBarcode(String barcode);

    @Query("SELECT p FROM ProductEntity p " +
            "WHERE (:barcode IS NULL OR p.barcode = :barcode) " +
            "AND (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:brand IS NULL OR LOWER(p.brand.name) LIKE LOWER(CONCAT('%', :brand, '%'))) " +
            "AND (:category IS NULL OR LOWER(p.category.name) LIKE LOWER(CONCAT('%', :category, '%'))) " +
            "AND (:active IS NULL OR p.active = :active)")
    Page<ProductEntity> findByFilters(@Param("barcode") String barcode,
                                      @Param("name") String name,
                                      @Param("brand") String brand,
                                      @Param("category") String category,
                                      @Param("active") Boolean active,
                                      Pageable pageable);
}
