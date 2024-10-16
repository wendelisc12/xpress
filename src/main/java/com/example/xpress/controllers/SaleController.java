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
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/sale")
public class SaleController {
    @Autowired
    SaleRepository saleRepository;

    @Autowired
    SaleService saleService;

    @GetMapping
    public List<Sale> getSales(){
        List<Sale> results = saleRepository.findAll();
        return results;
    }

    @PostMapping("/{cartId}")
    @Transactional
    public Sale makeSale(@PathVariable Long cartId, @RequestParam Long paymentId){
        Sale results = saleService.makeSale(cartId, paymentId);
        return results;
    }
}
