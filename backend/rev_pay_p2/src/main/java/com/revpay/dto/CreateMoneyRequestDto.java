package com.revpay.dto;

import java.math.BigDecimal;

public class CreateMoneyRequestDto {

	private String to; //email or phone no.
	private BigDecimal amount;
	private String purpose;
	
	public CreateMoneyRequestDto() {
		
	}
	
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	
}
