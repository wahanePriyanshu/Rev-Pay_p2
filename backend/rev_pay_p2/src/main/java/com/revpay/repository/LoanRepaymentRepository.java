package com.revpay.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.revpay.entity.LoanRepayment;

public interface LoanRepaymentRepository extends JpaRepository<LoanRepayment, Long> {
    List<LoanRepayment> findByLoanId(Long loanId);
}
