package com.revpay.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.revpay.entity.MoneyRequest;
import com.revpay.entity.User;

public interface MoneyRequestRepository extends JpaRepository<MoneyRequest, Long> {

	 List<MoneyRequest> findByReceiverOrderByCreatedAtDesc(User receiver);

	    List<MoneyRequest> findByRequesterOrderByCreatedAtDesc(User requester);
	    
}
