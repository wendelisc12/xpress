package com.example.xpress.service;

import com.example.xpress.entities.Product;
import com.example.xpress.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public ProductService(){
    }

    public Product addProduct(Product product){
        if(product.getQttStock() < 0){
            throw new RuntimeException("The product quantity cannot be less than zero.");
        }

        if(!product.isPerishable()){
            product.setExpirationDate(null);
        }else if(product.isPerishable() && product.getExpirationDate() == null){
            throw new RuntimeException("Perishable products must have an expiration date.");
        }

        if(productRepository.existsByName(product.getName())){
            throw new RuntimeException("This product already exists in the catalog.");
        }

        return productRepository.save(product);
    }

    public Product updateQttStock(Long id, int Qtt){
        Product product = productRepository.findById(id).get();

        if(Qtt < 0){
            throw new RuntimeException("You cannot enter values below 0.");
        }else{
            product.setQttStock(Qtt);
            Product newProduct = productRepository.save(product);
            return newProduct;
        }
    }

    public void downQttStock(Long id, int qttToDown){
        Product product = productRepository.findById(id).get();

        if(product.getQttStock() != 0){
            product.setQttStock(product.getQttStock() - qttToDown);
        }else{
            throw new RuntimeException("The stock is currently empty.");
        }

    }
}
