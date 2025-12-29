package com.scaffold.template.repositories;

import com.scaffold.template.entities.MovementStockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovementStockRepository extends JpaRepository<MovementStockEntity, Long> {
}
