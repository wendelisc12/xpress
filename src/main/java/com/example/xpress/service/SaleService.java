package com.example.xpress.service;

import com.example.xpress.entities.*;
import com.example.xpress.repository.*;
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
    ProductRepository productRepository;

    @Autowired
    ProductService productService;

    @Autowired
    TokenService tokenService;

    @Autowired
    InventoryTransactionRepository inventoryTransactionRepository;

    public Sale makeSale(String token, PaymentMethodEnum paymentMethod, int points){
        Cart cart = tokenService.getUserByToken(token).getCart();
        Users user = tokenService.getUserByToken(token);
        double discount = 0;

        if(points > user.getPoints()){
            throw new RuntimeException("you don't have sufficient points");
        }

        if(cart.getItems().isEmpty()){
            throw new RuntimeException("Cart is empty.");
        }

        if(points != 0){
            discount = usePoints(user, points, cart.getTotalPrice());
        }

        Sale newSale = new Sale();
        int earnedPoints = (int) (cart.getTotalPrice() * 5);
        newSale.setId(UUID.randomUUID().toString());
        newSale.setDate(new Date());
        newSale.setQuantity(cart.getTotalQuantity());
        newSale.setTotalPrice(cart.getTotalPrice() - discount);
        newSale.setPayment(paymentMethod.getPaymentMethod());
        newSale.setUser(user);
        newSale.setDiscount(discount);
        newSale.setEarnedPoints(earnedPoints);

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

        cart.clearCart();

        user.setPoints(user.getPoints() + earnedPoints);

        return sale;
    }

    public double usePoints(Users user,int points, double totalPrice){
        if(points >= 1000 && totalPrice >=45){
            user.setPoints(user.getPoints() - 1000);
            return 40.0;
        }else if(points >= 500 && totalPrice >= 20){
            user.setPoints(user.getPoints() - 500);
            return 15.0;
        }else if(points >= 250 && totalPrice >= 10){
            user.setPoints(user.getPoints() - 250);
            return 5;
        }

        throw new RuntimeException("you don't have sufficient points");
    }
}
