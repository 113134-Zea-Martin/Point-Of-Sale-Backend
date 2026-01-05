package com.scaffold.template.repositories;

import com.scaffold.template.entities.CashRegisterClosureEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface CashRegisterClosureRepository extends JpaRepository<CashRegisterClosureEntity, Long> {
    boolean existsByDate(LocalDate date);

    Page<CashRegisterClosureEntity> findAllByDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    Page<CashRegisterClosureEntity> findAllByDateGreaterThanEqual(LocalDate startDate, Pageable pageable);

    Page<CashRegisterClosureEntity> findAllByDateLessThanEqual(LocalDate endDate, Pageable pageable);

}
