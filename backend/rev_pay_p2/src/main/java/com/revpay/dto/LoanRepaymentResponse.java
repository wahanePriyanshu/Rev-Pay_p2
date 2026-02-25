package com.revpay.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class LoanRepaymentResponse {

	private Long id;
	private BigDecimal amount;
	private LocalDateTime paidAt;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public LocalDateTime getPaidAt() {
		return paidAt;
	}
	public void setPaidAt(LocalDateTime paidAt) {
		this.paidAt = paidAt;
	}
	
}
