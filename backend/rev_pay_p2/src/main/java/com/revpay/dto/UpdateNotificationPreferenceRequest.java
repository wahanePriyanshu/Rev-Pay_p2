package com.revpay.dto;

public class UpdateNotificationPreferenceRequest {

    private boolean transactionAlert;
    private boolean requestAlert;
    private boolean lowBalanceAlert;

    public boolean isTransactionAlert() { return transactionAlert; }
    public void setTransactionAlert(boolean transactionAlert) { this.transactionAlert = transactionAlert; }

    public boolean isRequestAlert() { return requestAlert; }
    public void setRequestAlert(boolean requestAlert) { this.requestAlert = requestAlert; }

    public boolean isLowBalanceAlert() { return lowBalanceAlert; }
    public void setLowBalanceAlert(boolean lowBalanceAlert) { this.lowBalanceAlert = lowBalanceAlert; }
}