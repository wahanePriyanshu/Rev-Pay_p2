package com.revpay.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.revpay.dto.ApplyLoanRequest;
import com.revpay.dto.LoanRepaymentResponse;
import com.revpay.dto.LoanResponse;
import com.revpay.dto.RepaymentRequest;
import com.revpay.entity.LoanApplication;
import com.revpay.entity.LoanRepayment;
import com.revpay.entity.User;
import com.revpay.entity.enums.LoanStatus;
import com.revpay.repository.LoanApplicationRepository;
import com.revpay.repository.LoanRepaymentRepository;
import com.revpay.repository.UserRepository;
import com.revpay.service.LoanService;

import jakarta.transaction.Transactional;

@Service
public class LoanServiceImpl implements LoanService {

    private final LoanApplicationRepository loanRepo;
    private final LoanRepaymentRepository repaymentRepo;
    private final UserRepository userRepository;

    public LoanServiceImpl(LoanApplicationRepository loanRepo,
                           LoanRepaymentRepository repaymentRepo,
                           UserRepository userRepository) {
        this.loanRepo = loanRepo;
        this.repaymentRepo = repaymentRepo;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public LoanResponse applyForLoan(ApplyLoanRequest request) {
        User businessUser = getCurrentUser();

        LoanApplication loan = new LoanApplication();
        loan.setBusinessUser(businessUser);
        loan.setAmount(request.getAmount());
        loan.setPurpose(request.getPurpose());
        loan.setTenureMonths(request.getTenureMonths());
        loan.setInterestRate(new BigDecimal("12.00")); // fixed for now
        loan.setStatus(LoanStatus.PENDING);

        LoanApplication saved = loanRepo.save(loan);
        return mapToResponse(saved);
    }

    @Override
    public List<LoanResponse> getMyLoans() {
        User businessUser = getCurrentUser();
        return loanRepo.findByBusinessUserId(businessUser.getId())
                .stream().map(this::mapToResponse).toList();
    }

    @Override
    public LoanResponse getLoanById(Long loanId) {
        User businessUser = getCurrentUser();

        LoanApplication loan = loanRepo.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        if (!loan.getBusinessUser().getId().equals(businessUser.getId())) {
            throw new RuntimeException("Not authorized");
        }

        return mapToResponse(loan);
    }

    @Override
    @Transactional
    public LoanRepaymentResponse repayLoan(Long loanId, RepaymentRequest request) {
        User businessUser = getCurrentUser();

        LoanApplication loan = loanRepo.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        if (!loan.getBusinessUser().getId().equals(businessUser.getId())) {
            throw new RuntimeException("Not authorized");
        }

        if (loan.getStatus() != LoanStatus.ACTIVE && loan.getStatus() != LoanStatus.APPROVED) {
            throw new RuntimeException("Loan is not active");
        }

        // Mark active if first payment
        if (loan.getStatus() == LoanStatus.APPROVED) {
            loan.setStatus(LoanStatus.ACTIVE);
        }

        LoanRepayment repayment = new LoanRepayment();
        repayment.setLoan(loan);
        repayment.setAmount(request.getAmount());

        LoanRepayment saved = repaymentRepo.save(repayment);

        LoanRepaymentResponse res = new LoanRepaymentResponse();
        res.setId(saved.getId());
        res.setAmount(saved.getAmount());
        res.setPaidAt(saved.getPaidAt());
        return res;
    }

    @Override
    public List<LoanRepaymentResponse> getRepayments(Long loanId) {
        return repaymentRepo.findByLoanId(loanId)
                .stream()
                .map(r -> {
                    LoanRepaymentResponse res = new LoanRepaymentResponse();
                    res.setId(r.getId());
                    res.setAmount(r.getAmount());
                    res.setPaidAt(r.getPaidAt());
                    return res;
                }).toList();
    }

    // -------- helpers --------

    private User getCurrentUser() {
        String emailOrPhone = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmailOrPhone(emailOrPhone, emailOrPhone)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private LoanResponse mapToResponse(LoanApplication loan) {
        LoanResponse res = new LoanResponse();
        res.setId(loan.getId());
        res.setAmount(loan.getAmount());
        res.setPurpose(loan.getPurpose());
        res.setTenureMonths(loan.getTenureMonths());
        res.setInterestRate(loan.getInterestRate());
        res.setStatus(loan.getStatus().name());
        res.setCreatedAt(loan.getCreatedAt());
        return res;
    }
}