package com.revpay.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class InvoiceResponse {
    private Long id;
    private Long businessUserId;
    private Long customerId;
    private BigDecimal totalAmount;
    private String status;
    private LocalDate dueDate;
    private LocalDateTime createdAt;
    private List<InvoiceItemResponse> items;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getBusinessUserId() {
		return businessUserId;
	}
	public void setBusinessUserId(Long businessUserId) {
		this.businessUserId = businessUserId;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public LocalDate getDueDate() {
		return dueDate;
	}
	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public List<InvoiceItemResponse> getItems() {
		return items;
	}
	public void setItems(List<InvoiceItemResponse> items) {
		this.items = items;
	}

   
}