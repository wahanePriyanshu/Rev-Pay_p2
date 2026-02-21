package com.revpay.repository;

import com.revpay.entity.User;
import com.revpay.entity.Wallet;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

	Optional<Wallet> findByUser(User user);
}