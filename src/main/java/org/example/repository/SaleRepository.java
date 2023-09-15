package org.example.repository;

import org.example.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
    List<Sale> findByProductPriceGreaterThan(Double price);
    @Modifying
    @Query("UPDATE Sale s SET s.totalValue = 0 WHERE s.totalValue IS NULL")
    void updateTotalValueToZero();
}

