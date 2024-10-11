package com.example.xpress.controllers;

import com.example.xpress.entities.Product;
import com.example.xpress.repository.ProductRepository;
import com.example.xpress.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Product> products(){
        List<Product> results = productRepository.findAll();
        return results;
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id){
        Product results = productRepository.findById(id).get();
        return results;
    }

    @PostMapping
    public Product addProduct(@RequestBody Product product){
        Product newProduct = productService.addProduct(product);
        return newProduct;
    }

    @DeleteMapping
    public void deleteProduct(@RequestParam(name = "id") Long id){
        productRepository.deleteById(id);
    }

}
