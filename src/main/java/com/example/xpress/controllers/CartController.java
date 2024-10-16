package com.example.xpress.controllers;

import com.example.xpress.entities.Cart;
import com.example.xpress.entities.Product;
import com.example.xpress.repository.CartRepository;
import com.example.xpress.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    CartService cartService;

    @PostMapping
    public Cart createCart(){
        Cart results = new Cart();
        results.setTotalPrice(0);
        results.setTotalPrice(0.0);
        cartRepository.save(results);
        return results;
    }

    @GetMapping("/{id}")
    public Cart getCart(@PathVariable Long id){
        Cart results = cartRepository.findById(id).orElseThrow(() -> new RuntimeException("Cart doesn't exists."));
        return results;
    }

    @PostMapping("/{cartId}/item")
    public Cart addProductToCart(@PathVariable Long cartId, @RequestParam(name = "pId") Long productId, @RequestParam(name = "qtt") int quantity){
        Cart results = cartService.addProductToCart(cartId, productId, quantity);
        return results;
    }
}
