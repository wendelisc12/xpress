package com.example.xpress.service;

import com.example.xpress.entities.*;
import com.example.xpress.repository.CartRepository;
import com.example.xpress.repository.PaymentRepository;
import com.example.xpress.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SaleService {

    @Autowired
    SaleRepository saleRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    PaymentRepository paymentRepository;

    public Sale makeSale(Long cartId, Long paymentId){
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new RuntimeException("Cart not found."));
        Payment payment =  paymentRepository.findById(paymentId).orElseThrow(()-> new RuntimeException("Payment method not found."));

        if(cart.getItems() == null){
            throw new RuntimeException("Cart is empty.");
        }

        Sale newSale = new Sale();
        newSale.setDate(new Date());
        newSale.setQuantity(cart.getTotalQuantity());
        newSale.setTotalPrice(cart.getTotalPrice());
        newSale.setPayment(payment);

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

        cartRepository.deleteById(cartId);

        return sale;
    }
}
