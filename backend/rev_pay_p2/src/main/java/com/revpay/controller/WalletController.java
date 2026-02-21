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
    public Wallet getMyWallet(Authentication authentication) {
        String username = authentication.getName(); // comes from JWT
        return walletService.getMyWallet(username);
    }

    // GET /api/wallet/balance
    @GetMapping("/balance")
    public Map<String, Object> getMyBalance(Authentication authentication) {
        String username = authentication.getName();
        Wallet wallet = walletService.getMyWallet(username);

        return Map.of(
                "walletId", wallet.getId(),
                "balance", wallet.getBalance()
        );
    }
    
    
    @PostMapping("/add")
    public ResponseEntity<?> addMoney(@RequestBody AddMoneyRequest request) {
        Wallet wallet = walletService.addMoney(request.getAmount());

        Map<String, Object> response = new HashMap<>();
        response.put("walletId", wallet.getId());
        response.put("balance", wallet.getBalance());

        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/withdraw")
    public ResponseEntity<?> withdrawMoney(@RequestBody AddMoneyRequest request) {
        Wallet wallet = walletService.withdrawMoney(request.getAmount());

        Map<String, Object> response = new HashMap<>();
        response.put("walletId", wallet.getId());
        response.put("balance", wallet.getBalance());

        return ResponseEntity.ok(response);
    }
    
    
    
    
    
    
}