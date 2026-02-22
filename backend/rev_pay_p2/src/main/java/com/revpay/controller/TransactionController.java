package com.revpay.controller;

import com.revpay.dto.TransactionResponse;
import com.revpay.service.TransactionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // GET /api/transactions/my
    @GetMapping("/my")
    public List<TransactionResponse> getMyTransactions() {
        return transactionService.getMyTransactions();
    }
}

