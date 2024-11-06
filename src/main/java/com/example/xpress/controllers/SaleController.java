package com.example.xpress.controllers;

import com.example.xpress.entities.Cart;
import com.example.xpress.entities.CartItem;
import com.example.xpress.entities.Sale;
import com.example.xpress.entities.SaleItem;
import com.example.xpress.repository.CartRepository;
import com.example.xpress.repository.SaleRepository;
import com.example.xpress.service.SaleService;
import jakarta.persistence.Entity;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/sale")
public class SaleController {
    @Autowired
    SaleRepository saleRepository;

    @Autowired
    SaleService saleService;

    @GetMapping
    public List<Sale> getSales(@RequestHeader("Authorization")String token){
        List<Sale> results = saleRepository.findAll();
        return results;
    }

    @GetMapping("/{id}")
    public Sale getSaleById(@RequestHeader("Authorization")String token, String id){
        System.out.println(id);
        return saleRepository.findById(id).orElseThrow(() -> new RuntimeException("Sale not found."));
    }

    @PostMapping
    @Transactional
    public ResponseEntity makeSale(@RequestHeader("Authorization") String token, @RequestParam Long paymentId){
        Sale results = saleService.makeSale(token, paymentId);
        Map<String, Object> response = new HashMap<>();
        response.put("sale", results);
        boolean isStockLow = results.getItems().stream().anyMatch(item -> item.getProduct().getQttStock() <= 10);

        if (isStockLow) {
            response.put("warning", "One or more products have less than 10 in stock.");
        }

        return ResponseEntity.ok(response);
    }
}
