package com.revpay.service;

import java.util.List;

import com.revpay.dto.ApplyLoanRequest;
import com.revpay.dto.LoanRepaymentResponse;
import com.revpay.dto.LoanResponse;
import com.revpay.dto.RepaymentRequest;

public interface LoanService {

    LoanResponse applyForLoan(ApplyLoanRequest request);

    List<LoanResponse> getMyLoans();

    LoanResponse getLoanById(Long loanId);

    LoanRepaymentResponse repayLoan(Long loanId, RepaymentRequest request);

    List<LoanRepaymentResponse> getRepayments(Long loanId);
}
