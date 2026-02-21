package com.revpay.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revpay.dto.AddMoneyRequest;
import com.revpay.dto.WalletResponse;
import com.revpay.entity.Wallet;
import com.revpay.service.WalletService;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    // GET /api/wallet/me
    @GetMapping("/me")
    public WalletResponse getMyWallet(Authentication authentication) {
        String username = authentication.getName();
        Wallet wallet = walletService.getMyWallet(username);

        return new WalletResponse(wallet.getId(), wallet.getBalance());
    }

    // GET /api/wallet/balance
    @GetMapping("/balance")
    public WalletResponse getMyBalance(Authentication authentication) {
        String username = authentication.getName();
        Wallet wallet = walletService.getMyWallet(username);

        return new WalletResponse(wallet.getId(), wallet.getBalance());
    }
    
    //Post/api/wallet/add - to add money in wallet
    @PostMapping("/add")
    public ResponseEntity<?> addMoney(@RequestBody AddMoneyRequest request) {
        Wallet wallet = walletService.addMoney(request.getAmount());
        return ResponseEntity.ok(new WalletResponse(wallet.getId(), wallet.getBalance()));
    }

    //Post/wallet/withdraw - to withdraw money from wallet
    @PostMapping("/withdraw")
    public ResponseEntity<?> withdrawMoney(@RequestBody AddMoneyRequest request) {
        Wallet wallet = walletService.withdrawMoney(request.getAmount());
        return ResponseEntity.ok(new WalletResponse(wallet.getId(), wallet.getBalance()));
    }
    
    
    
    
    
    
}