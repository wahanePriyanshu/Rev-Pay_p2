package com.revpay.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.revpay.entity.LoanApplication;

public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long> {
    List<LoanApplication> findByBusinessUserId(Long businessUserId);
}