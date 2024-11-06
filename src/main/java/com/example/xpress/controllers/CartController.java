package com.example.xpress.controllers;

import com.example.xpress.entities.Cart;
import com.example.xpress.entities.Product;
import com.example.xpress.entities.Users;
import com.example.xpress.repository.CartRepository;
import com.example.xpress.repository.UserRepository;
import com.example.xpress.service.CartService;
import com.example.xpress.service.TokenService;
import org.antlr.v4.runtime.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    CartService cartService;

    @Autowired
    TokenService tokenService;

    @Autowired
    UserRepository userRepository;

    @GetMapping
    public Cart getCart(@RequestHeader("Authorization")String token){
        Users user = tokenService.getUserByToken(token);
        if(user == null){
            throw new RuntimeException("Cart not found");
        }else{
            return user.getCart();
        }

    }

    @PostMapping("/item")
    public Cart addProductToCart(@RequestHeader("Authorization")  String token, @RequestParam(name = "pId") Long productId, @RequestParam(name = "qtt") int quantity){
        Cart results = cartService.addProductToCart(token, productId, quantity);
        return results;
    }

    @DeleteMapping("/{id}")
    public void deleteCart(@PathVariable Long id){
        cartRepository.deleteById(id);
    }
}
