package com.revpay.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revpay.dto.SendMoneyRequest;
import com.revpay.service.WalletService;

@RestController
@RequestMapping("/api/transfer")
public class TransferController {

	private final WalletService walletService;
	
	public TransferController(WalletService walletService) {
		this.walletService = walletService;
	}
	
	@PostMapping("/send")
    public ResponseEntity<?> sendMoney(@RequestBody SendMoneyRequest request) {
        walletService.sendMoney(request.getTo(), request.getAmount(),request.getPin());

        return ResponseEntity.ok(Map.of(
                "message", "Money sent successfully",
                "to", request.getTo(),
                "amount", request.getAmount()
        ));
    }
	
}
