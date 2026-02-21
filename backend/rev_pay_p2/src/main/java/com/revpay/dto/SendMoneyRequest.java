package com.revpay.dto;

import java.math.BigDecimal;

public class SendMoneyRequest {
    private String to;        // email or phone
    private BigDecimal amount;
    private String note;

    public String getTo() { return to; }
    public void setTo(String to) { this.to = to; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}