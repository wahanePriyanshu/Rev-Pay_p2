package com.revpay.dto;

import java.time.LocalDate;
import java.util.List;

public class CreateInvoiceRequest {
    private Long customerId;        // User ID of customer
    private LocalDate dueDate;
    private List<InvoiceItemRequest> items;
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public LocalDate getDueDate() {
		return dueDate;
	}
	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}
	public List<InvoiceItemRequest> getItems() {
		return items;
	}
	public void setItems(List<InvoiceItemRequest> items) {
		this.items = items;
	}

    
}