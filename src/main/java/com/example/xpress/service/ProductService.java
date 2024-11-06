package com.example.xpress.service;

import com.example.xpress.entities.InventoryTransaction;
import com.example.xpress.entities.MovimentType;
import com.example.xpress.entities.Product;
import com.example.xpress.entities.Users;
import com.example.xpress.repository.InventoryTransactionRepository;
import com.example.xpress.repository.ProductRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryTransactionRepository inventoryTransactionRepository;

    @Autowired
    private TokenService tokenService;

    public Product addProduct(String token, Product product){
        Users user = tokenService.getUserByToken(token);

        if(product.getQttStock() < 0){
            throw new RuntimeException("The product quantity cannot be less than zero.");
        }

        if(product.getExpirationDate().isBefore(LocalDate.now())){
            throw new RuntimeException("The expiration date cannot be expired.");
        }

        if(!product.isPerishable()){
            product.setExpirationDate(null);
        }else if(product.isPerishable() && product.getExpirationDate() == null){
            throw new RuntimeException("Perishable products must have an expiration date.");
        }

        if(productRepository.existsByName(product.getName())){
            throw new RuntimeException("This product already exists in the catalog.");
        }
        Product newProduct = productRepository.save(product);

        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setProduct(product);
        transaction.setMovimentDate(LocalDateTime.now());
        transaction.setUser(user);
        transaction.setMovimentType(MovimentType.INCOMING);
        transaction.setQuantity(product.getQttStock());

        inventoryTransactionRepository.save(transaction);

        return newProduct;
    }

    public Product addToStock(Long id, Integer QttToAdd, String token){
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found."));
        Users user = tokenService.getUserByToken(token);

        if(QttToAdd < 0){
            throw new RuntimeException("You cannot enter values below 0.");
        }

        product.setQttStock(product.getQttStock() + QttToAdd);
        Product productAtt = productRepository.save(product);

        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setProduct(product);
        transaction.setMovimentDate(LocalDateTime.now());
        transaction.setUser(user);
        transaction.setMovimentType(MovimentType.INCOMING);
        transaction.setQuantity(QttToAdd);
        inventoryTransactionRepository.save(transaction);

        return productAtt;
    }

    public Product updateQttStock(Long id, int Qtt, String token){
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found."));
        Users user = tokenService.getUserByToken(token);

        if(Qtt < 0){
            throw new RuntimeException("You cannot enter values below 0.");
        }

        product.setQttStock(Qtt);
        Product newProduct = productRepository.save(product);
        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setProduct(product);
        transaction.setMovimentDate(LocalDateTime.now());
        transaction.setUser(user);

        int oldQtt = product.getQttStock();

        if(Qtt > oldQtt){
            transaction.setMovimentType(MovimentType.INCOMING);
            transaction.setQuantity(Qtt);
        }else{
            transaction.setMovimentType(MovimentType.OUTGOING);
            transaction.setQuantity(Qtt);
        }

        inventoryTransactionRepository.save(transaction);
        return newProduct;

    }

    public void downQttStock(Product product, int qttToDown){
        if(product.getQttStock() != 0){
            product.setQttStock(product.getQttStock() - qttToDown);
        }else{
            throw new RuntimeException("The stock is currently empty.");
        }

    }
}
