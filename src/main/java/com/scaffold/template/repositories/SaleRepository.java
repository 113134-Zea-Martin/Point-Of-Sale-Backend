package com.scaffold.template.repositories;

import com.scaffold.template.entities.PaymentType;
import com.scaffold.template.entities.SaleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<SaleEntity,Long>{

    @Query("SELECT DISTINCT s FROM SaleEntity s LEFT JOIN s.saleDetails sd " +
            "WHERE (:userId IS NULL OR s.user.id = :userId) " +
            "AND (:paymentType IS NULL OR s.paymentType = :paymentType) " +
            "AND (:fromDate IS NULL OR s.dateTime >= :fromDate) " +
            "AND (:toDate IS NULL OR s.dateTime <= :toDate) " +
            "AND (:minTotal IS NULL OR s.total >= :minTotal) " +
            "AND (:maxTotal IS NULL OR s.total <= :maxTotal) ")
    Page<SaleEntity> findSalesByFilters(@Param("userId") Long userId,
                                        @Param("paymentType") PaymentType paymentType,
                                        @Param("fromDate") LocalDateTime fromDate,
                                        @Param("toDate") LocalDateTime toDate,
                                        @Param("minTotal") Double minTotal,
                                        @Param("maxTotal") Double maxTotal,
                                        Pageable pageable
    );

    List<SaleEntity> findByDateTimeBetween(LocalDateTime fromDate, LocalDateTime toDate);
}
