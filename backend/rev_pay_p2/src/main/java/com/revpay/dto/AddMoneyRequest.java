package com.revpay.dto;

import java.math.BigDecimal;

public class AddMoneyRequest {
    private BigDecimal amount;
    
    private String pin;
    
    

    public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}