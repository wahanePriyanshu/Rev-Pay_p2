package com.revpay.service;

import java.util.List;

import com.revpay.dto.TransactionResponse;

public interface TransactionService {

	List<TransactionResponse> getMyTransactions();
}
