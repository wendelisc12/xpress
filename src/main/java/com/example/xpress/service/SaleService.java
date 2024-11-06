package com.example.xpress.service;

import com.example.xpress.entities.*;
import com.example.xpress.repository.*;
import org.antlr.v4.runtime.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class SaleService {

    @Autowired
    SaleRepository saleRepository;

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductService productService;

    @Autowired
    TokenService tokenService;

    @Autowired
    InventoryTransactionRepository inventoryTransactionRepository;

    public Sale makeSale(String token, Long paymentId){
        Cart cart = tokenService.getUserByToken(token).getCart();
        Payment payment =  paymentRepository.findById(paymentId).orElseThrow(()-> new RuntimeException("Payment method not found."));
        Users user = tokenService.getUserByToken(token);

        if(cart.getItems().isEmpty()){
            throw new RuntimeException("Cart is empty.");
        }

        for(CartItem cartItem : cart.getItems()){
            Product product = cartItem.getProduct();
            productService.downQttStock(product, cartItem.getQuantity());
            productRepository.save(product);

            InventoryTransaction transaction = new InventoryTransaction();
            transaction.setQuantity(cartItem.getQuantity());
            transaction.setMovimentType(MovimentType.OUTGOING);
            transaction.setProduct(product);
            transaction.setUser(user);
            transaction.setMovimentDate(LocalDateTime.now());
            inventoryTransactionRepository.save(transaction);
        }

        Sale newSale = new Sale();
        newSale.setId(UUID.randomUUID().toString());
        newSale.setDate(new Date());
        newSale.setQuantity(cart.getTotalQuantity());
        newSale.setTotalPrice(cart.getTotalPrice());
        newSale.setPayment(payment);
        newSale.setUser(user);

        List<SaleItem> saleItemList = new ArrayList<>();

        for(CartItem cartItem : cart.getItems()){
            SaleItem saleItem = new SaleItem();

            saleItem.setSale(newSale);
            saleItem.setProduct(cartItem.getProduct());
            saleItem.setQuantity(cartItem.getQuantity());
            saleItem.setPrice(cartItem.getPrice());

            saleItemList.add(saleItem);
        }

        newSale.setItems(saleItemList);
        Sale sale = saleRepository.save(newSale);

        cart.clearCart();

        return sale;
    }
}
