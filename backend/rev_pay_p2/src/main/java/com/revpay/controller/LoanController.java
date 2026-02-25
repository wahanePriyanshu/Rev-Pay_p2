package com.revpay.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revpay.dto.ApplyLoanRequest;
import com.revpay.dto.LoanRepaymentResponse;
import com.revpay.dto.LoanResponse;
import com.revpay.service.LoanService;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping
    public ResponseEntity<LoanResponse> apply(@RequestBody ApplyLoanRequest request) {
        return ResponseEntity.ok(loanService.applyForLoan(request));
    }

    @GetMapping
    public ResponseEntity<List<LoanResponse>> myLoans() {
        return ResponseEntity.ok(loanService.getMyLoans());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(loanService.getLoanById(id));
    }

//    @PostMapping("/{id}/repay")
//    public ResponseEntity<LoanRepaymentResponse> repay(@PathVariable Long id,
//                                                       @RequestBody RepaymentRequest request) {
//        return ResponseEntity.ok(loanService.repayLoan(id, request));
//    }

    @GetMapping("/{id}/repayments")
    public ResponseEntity<List<LoanRepaymentResponse>> repayments(@PathVariable Long id) {
        return ResponseEntity.ok(loanService.getRepayments(id));
    }
}