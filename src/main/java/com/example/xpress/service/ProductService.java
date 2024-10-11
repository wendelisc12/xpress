package com.example.xpress.service;

import com.example.xpress.entities.Product;
import com.example.xpress.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public ProductService(){
    }

    public Product addProduct(Product product){
        if(product.getQttStock() < 0){
            throw new RuntimeException("A quantidade de produto não pode ser inferior a zero.");
        }

        if(!product.isPerishable()){
            product.setExpirationDate(null);
        }else if(product.isPerishable() && product.getExpirationDate() == null){
            throw new RuntimeException("Produtos perecíveis precisam ter uma data de validade.");
        }

        if(productRepository.existsByName(product.getName())){
            throw new RuntimeException("Esse produto já existe no catálogo");
        }

        return productRepository.save(product);

    }
}
