package com.revpay.service;

import java.util.List;

import com.revpay.dto.CreateMoneyRequestDto;
import com.revpay.dto.MoneyRequestResponse;
import java.math.BigDecimal;

public interface MoneyRequestService {

	MoneyRequestResponse createRequest(CreateMoneyRequestDto dto);
	
	List<MoneyRequestResponse> getIncomingRequests();
    List<MoneyRequestResponse> getOutgoingRequests();
    
    void acceptRequest(Long requestId);
    void declineRequest(Long requestId);
    
	
}
