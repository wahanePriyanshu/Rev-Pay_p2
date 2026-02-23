package com.revpay.service.impl;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.revpay.dto.CreateMoneyRequestDto;
import com.revpay.dto.MoneyRequestResponse;
import com.revpay.entity.MoneyRequest;
import com.revpay.entity.User;
import com.revpay.repository.MoneyRequestRepository;
import com.revpay.repository.UserRepository;
import com.revpay.service.MoneyRequestService;
import java.util.stream.Collectors;

@Service
public class MoneyRequestServiceImpl implements MoneyRequestService {


	private final MoneyRequestRepository moneyRequestRepository;
    private final UserRepository userRepository;

    public MoneyRequestServiceImpl(MoneyRequestRepository moneyRequestRepository,
                                   UserRepository userRepository) {
        this.moneyRequestRepository = moneyRequestRepository;
        this.userRepository = userRepository;
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

}
