package com.example.xpress.controllers;

import com.example.xpress.entities.InventoryTransaction;
import com.example.xpress.repository.InventoryTransactionRepository;
import com.example.xpress.views.Views;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/inventoryTransaction")
public class InventoryTransactionController {

    @Autowired
    InventoryTransactionRepository inventoryTransactionRepository;

    @GetMapping
    @JsonView(Views.Internal.class)
    public List<InventoryTransaction> getTransactions(){
        List<InventoryTransaction> results = inventoryTransactionRepository.findAll();
        return results;
    }

}
