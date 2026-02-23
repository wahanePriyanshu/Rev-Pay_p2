package com.revpay.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revpay.dto.CreateMoneyRequestDto;
import com.revpay.dto.MoneyRequestResponse;
import com.revpay.service.MoneyRequestService;


@RestController
@RequestMapping("/api/requests")
public class MoneyRequestController {

	private final MoneyRequestService moneyRequestService;

	public MoneyRequestController(MoneyRequestService moneyRequestService) {
	
		this.moneyRequestService = moneyRequestService;
	}
	
	//create request code 
	@PostMapping
	public MoneyRequestResponse create (@RequestBody CreateMoneyRequestDto dto) {
		return moneyRequestService.createRequest(dto);
	}
	
	
	//incoming request (user asking me )
	@GetMapping("/incoming")
	public List<MoneyRequestResponse> incoming(){
		return moneyRequestService.getIncomingRequests();
	}
	
	
	//outgoing request (I asked other user for request)
	@GetMapping("/outgoing")
	public List<MoneyRequestResponse >outgoing(){
		return moneyRequestService.getOutgoingRequests();
	}
}
