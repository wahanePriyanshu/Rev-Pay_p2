package com.revpay.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.revpay.dto.CreateMoneyRequestDto;
import com.revpay.dto.MoneyRequestResponse;
import com.revpay.entity.MoneyRequest;
import com.revpay.entity.User;
import com.revpay.entity.Wallet;
import com.revpay.repository.MoneyRequestRepository;
import com.revpay.repository.TransactionRepository;
import com.revpay.repository.UserRepository;
import com.revpay.repository.WalletRepository;
import com.revpay.service.MoneyRequestService;
import com.revpay.entity.Transaction;
import com.revpay.entity.TransactionType;
import com.revpay.entity.TransactionStatus;


import jakarta.transaction.Transactional;

@Service
public class MoneyRequestServiceImpl implements MoneyRequestService {


	private final MoneyRequestRepository moneyRequestRepository;
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    public MoneyRequestServiceImpl(MoneyRequestRepository moneyRequestRepository,
                                   UserRepository userRepository,
                                   WalletRepository walletRepository,
                                   TransactionRepository transactionRepository) {
        this.moneyRequestRepository = moneyRequestRepository;
        this.userRepository = userRepository;
		this.walletRepository = walletRepository;
		this.transactionRepository = transactionRepository;
    }
	

	
	private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        return userRepository.findByEmail(username)
                .or(() -> userRepository.findByPhone(username))
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
	
	
	@Override
	public MoneyRequestResponse createRequest(CreateMoneyRequestDto dto) {
		
		if(dto.getAmount()==null || dto.getAmount().signum()<=0) {
			throw new RuntimeException("Amount must be greater than zero");
		}
		
		User requester =getCurrentUser();
		User receiver = userRepository.findByEmail(dto.getTo())
				.or(()-> userRepository.findByPhone(dto.getTo()))
				.orElseThrow(()-> new RuntimeException("Reciver not found :"+ dto.getTo()));
		
		
		if (requester.getId().equals(receiver .getId())) {
            throw new RuntimeException("You cannot request money from yourself");
        }
		
		MoneyRequest req = new MoneyRequest();
		req.setRequester(requester);
		req.setReceiver(receiver);
		req.setAmount(dto.getAmount());
		req.setPurpose(dto.getPurpose());
		req.setStatus(MoneyRequest.Status.PENDING);
		req.setCreatedAt(LocalDateTime.now());
		
		MoneyRequest saved = moneyRequestRepository.save(req);
		
		return new MoneyRequestResponse(
                saved.getId(),
                requester.getEmail(),
                receiver.getEmail(),
                saved.getAmount(),
                saved.getStatus().name(),
                saved.getPurpose(),
                saved.getCreatedAt()
        );
		

		
		
	}

	@Override
	public List<MoneyRequestResponse> getIncomingRequests() {
		User user = getCurrentUser();

        return moneyRequestRepository.findByReceiverOrderByCreatedAtDesc(user)
                .stream()
                .map(req -> new MoneyRequestResponse(
                        req.getId(),
                        req.getRequester().getEmail(),
                        req.getReceiver().getEmail(),
                        req.getAmount(),
                        req.getStatus().name(),
                        req.getPurpose(),
                        req.getCreatedAt()
                ))
                .collect(Collectors.toList());
	}

	@Override
	public List<MoneyRequestResponse> getOutgoingRequests() {
		User user = getCurrentUser();

        return moneyRequestRepository.findByRequesterOrderByCreatedAtDesc(user)
                .stream()
                .map(req -> new MoneyRequestResponse(
                        req.getId(),
                        req.getRequester().getEmail(),
                        req.getReceiver().getEmail(),
                        req.getAmount(),
                        req.getStatus().name(),
                        req.getPurpose(),
                        req.getCreatedAt()
                ))
                .collect(Collectors.toList());	}



	@Transactional
	@Override
	public void acceptRequest(Long requestId) {
		User current = getCurrentUser();
		
		MoneyRequest req = moneyRequestRepository.findById(requestId)
				.orElseThrow(()-> new RuntimeException("Request not found"));
		
		if(req.getStatus() != MoneyRequest.Status.PENDING) {
			throw new RuntimeException("Request already processed");
			
		}
		
		if(!req.getReceiver().getId().equals(current.getId())) {
			throw new RuntimeException("You are not allowed to accept this request");
		}
		BigDecimal amount = req.getAmount();
		
		Wallet receiverWallet = walletRepository.findByUser(current)
		        .orElseThrow(() -> new RuntimeException("Receiver wallet not found"));

		Wallet requesterWallet = walletRepository.findByUser(req.getRequester())
		        .orElseThrow(() -> new RuntimeException("Requester wallet not found"));
		
	    if (receiverWallet.getBalance().compareTo(amount) < 0) {
	        throw new RuntimeException("Insufficient balance");
	    }
	    
	    //Transfer Money 
	    receiverWallet.setBalance(receiverWallet.getBalance().subtract(amount));
	    requesterWallet.setBalance(requesterWallet.getBalance().add(amount));
		
	    walletRepository.save(receiverWallet);
	    walletRepository.save(requesterWallet);

	    //  Create SEND transaction
	    Transaction sendTx = new Transaction();
	    sendTx.setFromUser(current);
	    sendTx.setToUser(req.getRequester());
	    sendTx.setAmount(amount);
	    sendTx.setType(TransactionType.SEND);
	    sendTx.setStatus(TransactionStatus.SUCCESS);
	    sendTx.setNote("Money request accepted");
	    transactionRepository.save(sendTx);

	    // Create RECEIVE transaction
	    Transaction receiveTx = new Transaction();
	    receiveTx.setFromUser(current);
	    receiveTx.setToUser(req.getRequester());
	    receiveTx.setAmount(amount);
	    receiveTx.setType(TransactionType.RECEIVE);
	    receiveTx.setStatus(TransactionStatus.SUCCESS);
	    receiveTx.setNote("Money request accepted");
	    transactionRepository.save(receiveTx);

	    //  Update request status
	    req.setStatus(MoneyRequest.Status.ACCEPTED);
	    moneyRequestRepository.save(req);

	  
	}



	@Override
	public void declineRequest(Long requestId) {
		
		User current = getCurrentUser();

	    MoneyRequest req = moneyRequestRepository.findById(requestId)
	            .orElseThrow(() -> new RuntimeException("Request not found"));

	    if (req.getStatus() != MoneyRequest.Status.PENDING) {
	        throw new RuntimeException("Request already processed");
	    }

	    if (!req.getReceiver().getId().equals(current.getId())) {
	        throw new RuntimeException("You are not allowed to decline this request");
	    }

	    req.setStatus(MoneyRequest.Status.DECLINED);
	    moneyRequestRepository.save(req);
		
		
		
		
		
		
		
	}

}
