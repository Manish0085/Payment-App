package com.paypal.transaction.controller;


import com.paypal.common.entity.Transaction;
import com.paypal.transaction.service.ITransactionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final ITransactionService service;


    public TransactionController(ITransactionService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody Transaction transaction){
        Transaction created = service.createTransaction(transaction);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Transaction>> getAllTransaction(){
        List<Transaction> transactions = service.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }
}
