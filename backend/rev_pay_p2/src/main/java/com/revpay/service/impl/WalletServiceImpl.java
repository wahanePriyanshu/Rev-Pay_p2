package com.revpay.service.impl;

import java.math.BigDecimal;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.revpay.entity.User;
import com.revpay.entity.Wallet;
import com.revpay.repository.UserRepository;
import com.revpay.repository.WalletRepository;
import com.revpay.service.WalletService;

@Service
public class WalletServiceImpl implements WalletService {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;

    public WalletServiceImpl(UserRepository userRepository, WalletRepository walletRepository) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
    }
    
    
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // email or phone from JWT

        return userRepository.findByEmail(username)
                .or(() -> userRepository.findByPhone(username))
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }
    
    
    

    @Override
    public Wallet getMyWallet(String username) {
        User user = userRepository.findByEmail(username)
                .or(() -> userRepository.findByPhone(username))
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        return walletRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Wallet not found for user"));
    }



	@Override
	public Wallet addMoney(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount must be greater than zero");
        }

        User user = getCurrentUser();   // ✅ now this method exists
        Wallet wallet = walletRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        wallet.setBalance(wallet.getBalance().add(amount));
        return walletRepository.save(wallet);
    }


	@Override
	public Wallet withdrawMoney(BigDecimal amount) {
		if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
	        throw new RuntimeException("Amount must be greater than zero");
	    }

	    User user = getCurrentUser();
	    Wallet wallet = walletRepository.findByUser(user)
	            .orElseThrow(() -> new RuntimeException("Wallet not found"));

	    if (wallet.getBalance().compareTo(amount) < 0) {
	        throw new RuntimeException("Insufficient balance");
	    }

	    wallet.setBalance(wallet.getBalance().subtract(amount));
	    return walletRepository.save(wallet);
	}


	@Override
	public void sendMoney(String to, BigDecimal amount) {
	    if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
	        throw new RuntimeException("Amount must be greater than zero");
	    }

	    // 1. Sender
	    User sender = getCurrentUser();
	    Wallet senderWallet = walletRepository.findByUser(sender)
	            .orElseThrow(() -> new RuntimeException("Sender wallet not found"));

	    // 2. Receiver
	    User receiver = userRepository.findByEmail(to)
	            .or(() -> userRepository.findByPhone(to))
	            .orElseThrow(() -> new RuntimeException("Receiver not found: " + to));

	    if (sender.getId().equals(receiver.getId())) {
	        throw new RuntimeException("Cannot send money to yourself");
	    }

	    Wallet receiverWallet = walletRepository.findByUser(receiver)
	            .orElseThrow(() -> new RuntimeException("Receiver wallet not found"));

	    // 3. Check balance
	    if (senderWallet.getBalance().compareTo(amount) < 0) {
	        throw new RuntimeException("Insufficient balance");
	    }

	    // 4. Transfer
	    senderWallet.setBalance(senderWallet.getBalance().subtract(amount));
	    receiverWallet.setBalance(receiverWallet.getBalance().add(amount));

	    // 5. Save both
	    walletRepository.save(senderWallet);
	    walletRepository.save(receiverWallet);

	    // (Later we’ll add Transaction record here)
	}
}