package com.revpay.service.impl;

import com.revpay.dto.TransactionResponse;
import com.revpay.entity.Transaction;
import com.revpay.entity.User;
import com.revpay.repository.TransactionRepository;
import com.revpay.repository.UserRepository;
import com.revpay.service.TransactionService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        return userRepository.findByEmail(username)
                .or(() -> userRepository.findByPhone(username))
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }

    @Override
    public List<TransactionResponse> getMyTransactions() {
        User user = getCurrentUser();

        List<Transaction> list =
                transactionRepository.findByFromUserOrToUserOrderByCreatedAtDesc(user, user);

        return list.stream()
                .map(tx -> new TransactionResponse(
                        tx.getId(),
                        tx.getType(),           // enum TransactionType
                        tx.getAmount(),
                        tx.getFromUser() != null ? tx.getFromUser().getEmail() : "SYSTEM",
                        tx.getToUser() != null ? tx.getToUser().getEmail() : "SYSTEM",
                        tx.getNote(),
                        tx.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }
}