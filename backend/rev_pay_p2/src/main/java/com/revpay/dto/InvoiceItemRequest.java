package com.revpay.dto;

import java.math.BigDecimal;

public class InvoiceItemRequest {
    private String description;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal tax; // percentage or flat as per your DB design
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public BigDecimal getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}
	public BigDecimal getTax() {
		return tax;
	}
	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}

    
}