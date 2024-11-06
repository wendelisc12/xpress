package com.example.xpress.repository;

import com.example.xpress.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByName(String name);
    List<Product> findByNameStartingWith(String name);
    List<Product> findByCategoryId(Long id);
    Product findByName(String name);
}
