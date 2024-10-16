package com.example.xpress.controllers;

import com.example.xpress.entities.Payment;
import com.example.xpress.repository.PaymentRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
    @RequestMapping("/paymentMethods")
public class PaymentController {

    @Autowired
    PaymentRepository paymentRepository;

    @GetMapping
    public List<Payment> getPaymentMethods(){
        List<Payment> results = paymentRepository.findAll();
        return results;
    }

    @PostMapping
    public Payment createPayment(@Valid @RequestBody Payment payment){
        Payment results = paymentRepository.save(payment);
        return results;
    }

}
