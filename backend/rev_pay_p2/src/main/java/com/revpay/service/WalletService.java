package com.revpay.service;

import java.math.BigDecimal;

import com.revpay.entity.Wallet;

public interface WalletService {
    Wallet getMyWallet(String username);
   
    Wallet addMoney(BigDecimal amount);
    
    Wallet withdrawMoney(BigDecimal amount);
    
    void sendMoney(String to,BigDecimal amount);
}