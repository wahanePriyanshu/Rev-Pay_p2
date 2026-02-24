package com.revpay.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.revpay.entity.Transaction;
import com.revpay.entity.TransactionStatus;
import com.revpay.entity.TransactionType;
import com.revpay.entity.User;
import com.revpay.entity.Wallet;
import com.revpay.repository.TransactionRepository;
import com.revpay.repository.UserRepository;
import com.revpay.repository.WalletRepository;
import com.revpay.service.NotificationService;
import com.revpay.service.WalletService;

@Service
public class WalletServiceImpl implements WalletService {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final NotificationService notificationService;
    private final PasswordEncoder passwordEncoder;
    

    public WalletServiceImpl(UserRepository userRepository, 
    		WalletRepository walletRepository,
    		TransactionRepository transactionRepository,
    		NotificationService notificationService,
    		PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
        this.notificationService= notificationService;
        this.passwordEncoder = passwordEncoder;
    }
    
    
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // email or phone from JWT

        return userRepository.findByEmail(username)
                .or(() -> userRepository.findByPhone(username))
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }
    
    private void verifyTransactionPin(User user, String rawPin) {
        if (user.getTransactionPin() == null) {
            throw new RuntimeException("Transaction PIN is not set");
        }
        if (rawPin == null || rawPin.isBlank()) {
            throw new RuntimeException("Transaction PIN is required");
        }
        if (!passwordEncoder.matches(rawPin, user.getTransactionPin())) {
            throw new RuntimeException("Invalid transaction PIN");
        }
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

        User user = getCurrentUser();
        Wallet wallet = walletRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        wallet.setBalance(wallet.getBalance().add(amount));
        walletRepository.save(wallet);

        Transaction tx = new Transaction();
        tx.setFromUser(null); // SYSTEM
        tx.setToUser(user);
        tx.setAmount(amount);
        tx.setType(TransactionType.ADD_FUNDS);
        tx.setStatus(TransactionStatus.SUCCESS);
        tx.setNote("Add money");
        tx.setCreatedAt(LocalDateTime.now());

        transactionRepository.save(tx);
        
        notificationService.createNotification(
        	    user.getId(),
        	    "",
        	    "Money Added",
        	    "₹" + amount + " added to your wallet"
        	);
        
        
        

        return wallet;
    }

    @Override
    public Wallet withdrawMoney(BigDecimal amount,String pin) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount must be greater than zero");
        }

        User user = getCurrentUser();
        verifyTransactionPin(user,pin);
        Wallet wallet = walletRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        wallet.setBalance(wallet.getBalance().subtract(amount));
        walletRepository.save(wallet);

        Transaction tx = new Transaction();
        tx.setFromUser(user);
        tx.setToUser(null); // BANK
        tx.setAmount(amount);
        tx.setType(TransactionType.WITHDRAW);
        tx.setStatus(TransactionStatus.SUCCESS);
        tx.setNote("Withdraw money");
        tx.setCreatedAt(LocalDateTime.now());

        transactionRepository.save(tx);
        notificationService.createNotification(
        	    user.getId(),
        	    "",
        	    "Money Withdrawn",
        	    "₹" + amount + " withdrawn from your wallet"
        	);
        
        

        return wallet;
    }


    @Override
    public void sendMoney(String to, BigDecimal amount ,String pin) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount must be greater than zero");
        }

        User sender = getCurrentUser();
        verifyTransactionPin(sender,pin);
        Wallet senderWallet = walletRepository.findByUser(sender)
                .orElseThrow(() -> new RuntimeException("Sender wallet not found"));

        User receiver = userRepository.findByEmail(to)
                .or(() -> userRepository.findByPhone(to))
                .orElseThrow(() -> new RuntimeException("Receiver not found: " + to));

        if (sender.getId().equals(receiver.getId())) {
            throw new RuntimeException("Cannot send money to yourself");
        }

        Wallet receiverWallet = walletRepository.findByUser(receiver)
                .orElseThrow(() -> new RuntimeException("Receiver wallet not found"));

        if (senderWallet.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        senderWallet.setBalance(senderWallet.getBalance().subtract(amount));
        receiverWallet.setBalance(receiverWallet.getBalance().add(amount));

        walletRepository.save(senderWallet);
        walletRepository.save(receiverWallet);

        // Sender TX
        Transaction senderTx = new Transaction();
        senderTx.setFromUser(sender);
        senderTx.setToUser(receiver);
        senderTx.setAmount(amount);
        senderTx.setType(TransactionType.SEND);
        senderTx.setStatus(TransactionStatus.SUCCESS);
        senderTx.setNote("Transfer");
        senderTx.setCreatedAt(LocalDateTime.now());
        transactionRepository.save(senderTx);
        
     // Notify sender
        notificationService.createNotification(
            sender.getId(),
            "TRANSACTION",
            "Money Sent",
            "You sent ₹" + amount + " to " + receiver.getEmail()
        );

        // Receiver TX
        Transaction receiverTx = new Transaction();
        receiverTx.setFromUser(sender);
        receiverTx.setToUser(receiver);
        receiverTx.setAmount(amount);
        receiverTx.setType(TransactionType.RECEIVE);
        receiverTx.setStatus(TransactionStatus.SUCCESS);
        receiverTx.setNote("Transfer");
        receiverTx.setCreatedAt(LocalDateTime.now());
        transactionRepository.save(receiverTx);
        

     // Notify receiver
     notificationService.createNotification(
         receiver.getId(),
         "TRANSACTION",
         "Money Received",
         "You received ₹" + amount + " from " + sender.getEmail()
     );
      
        
    }
    
    
    
    
}