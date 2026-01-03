package com.scaffold.template.repositories;

import com.scaffold.template.entities.MovementStockEntity;
import com.scaffold.template.entities.MovementType;
import com.scaffold.template.entities.Reason;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface MovementStockRepository extends JpaRepository<MovementStockEntity, Long> {

    @Query("""
        SELECT m FROM MovementStockEntity m
        WHERE (:productName IS NULL OR LOWER(m.product.name) LIKE LOWER(CONCAT('%', :productName, '%')))
          AND (:movementType IS NULL OR m.movementType = :movementType)
          AND (:reason IS NULL OR m.reason = :reason)
          AND (:userId IS NULL OR m.user.id = :userId)
          AND (:fromDate IS NULL OR m.date >= :fromDate)
          AND (:toDate IS NULL OR m.date <= :toDate)
        """)
    Page<MovementStockEntity> findByFilters(
            @Param("productName") String productName,
            @Param("movementType") MovementType movementType,
            @Param("reason") Reason reason,
            @Param("userId") Long userId,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate,
            Pageable pageable
    );

    @Query("""
        SELECT COALESCE(
          SUM(
            CASE
              WHEN m.movementType = :inType THEN m.quantity
              WHEN m.movementType = :outType THEN -m.quantity
              ELSE 0
            END
          ), 0)
        FROM MovementStockEntity m
        WHERE m.product.id = :productId AND m.date <= :date
        """)
    Integer getStockUntil(
            @Param("productId") Long productId,
            @Param("date") LocalDateTime date,
            @Param("inType") MovementType inType,
            @Param("outType") MovementType outType
    );

}
