package com.example.xpress.controllers;

import com.example.xpress.entities.Product;
import com.example.xpress.repository.ProductRepository;
import com.example.xpress.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> products(@RequestParam(required = false) String q){
        if(q == null){
            List<Product> results = productRepository.findAll();
            return results;
        }else{
            List<Product> results = productRepository.findByNameStartingWith(q);
            return results;
        }
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id){
        Product results = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found."));
        return results;
    }

    @PostMapping("/addStock/{id}")
    public Product addToStock(@RequestParam Integer qtt, @PathVariable Long id, @RequestHeader("Authorization") String token){
        Product results = productService.addToStock(id, qtt, token);
        return results;
    }

    @PostMapping
    public Product addProduct(@Valid @RequestBody Product product, @RequestHeader("Authorization") String token){
        Product newProduct = productService.addProduct(token,product);
        return newProduct;
    }

    @PatchMapping
    public Product addQttStock(@PathVariable(name = "id") Long id, @RequestParam(name = "Qtt") int Qtt, @RequestHeader String token){
        Product product = productService.updateQttStock(id, Qtt, token);
        return product;
    }

    @DeleteMapping
    public ResponseEntity<String> deleteProduct(@RequestParam(name = "id") Long id){
        productRepository.deleteById(id);
        return ResponseEntity.ok("Product was successfully deleted.");
    }

}
