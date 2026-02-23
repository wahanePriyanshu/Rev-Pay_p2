package com.revpay.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MoneyRequestResponse {
	private Long id;
	private String from;
	private String to;
	private BigDecimal amount;
	private String status;
	private String purpose;
	private LocalDateTime createdAt;
	public MoneyRequestResponse(Long id, String from, String to, BigDecimal amount, String status, String purpose,
			LocalDateTime createdAt) {
		super();
		this.id = id;
		this.from = from;
		this.to = to;
		this.amount = amount;
		this.status = status;
		this.purpose = purpose;
		this.createdAt = createdAt;
	}
	
	public MoneyRequestResponse() {}
	public Long getId() {
		return id;
	}
	public String getFrom() {
		return from;
	}
	public String getTo() {
		return to;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public String getStatus() {
		return status;
	}
	public String getPurpose() {
		return purpose;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	
	
	

}
