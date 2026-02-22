package com.revpay.repository;

import com.revpay.entity.Transaction;
import com.revpay.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByFromUserOrToUserOrderByCreatedAtDesc(User fromUser, User toUser);
}