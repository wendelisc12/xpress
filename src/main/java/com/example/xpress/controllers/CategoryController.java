package com.example.xpress.controllers;

import com.example.xpress.entities.Category;
import com.example.xpress.entities.Product;
import com.example.xpress.repository.CategoryRepository;
import com.example.xpress.repository.ProductRepository;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductRepository productRepository;

    @GetMapping
    public List<Category> categories(){
        List<Category> results = categoryRepository.findAll();
        return results;
    }

    @GetMapping("/{id}")
    public Category getCategoryById(@PathVariable Long id){
        Category results = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found."));
        return results;
    }

    @GetMapping("/{id}/products")
    public List<Product> getProductsByCategory(@PathVariable Long id){
        List<Product> results = productRepository.findByCategoryId(id);
        return results;
    }

    @PostMapping
    public Category addCategory(@Valid @RequestBody Category category){
        Category results = categoryRepository.save(category);
        return results;
    }

    @DeleteMapping
    public ResponseEntity<String> deleteCategoryById(@RequestParam Long id){
        categoryRepository.deleteById(id);
        return ResponseEntity.ok("Category was successfully deleted.");
    }
}
