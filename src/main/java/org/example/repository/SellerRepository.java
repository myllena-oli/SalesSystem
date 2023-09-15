package org.example.repository;

import org.example.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {
    @Query("SELECT s.salary FROM Seller s ORDER BY s.salary DESC")
    List<Double> findAllSalariesOrderBySalaryDesc();
}

