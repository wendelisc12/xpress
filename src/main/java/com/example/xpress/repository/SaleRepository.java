package com.example.xpress.repository;

import com.example.xpress.entities.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, String> {
    List<Sale> findByPaymentId(Long id);
}
