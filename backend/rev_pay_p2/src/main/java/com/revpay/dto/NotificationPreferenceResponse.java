package com.revpay.dto;

public class NotificationPreferenceResponse {

    private boolean transactionAlert;
    private boolean requestAlert;
    private boolean lowBalanceAlert;

    public NotificationPreferenceResponse(boolean transactionAlert, boolean requestAlert, boolean lowBalanceAlert) {
        this.transactionAlert = transactionAlert;
        this.requestAlert = requestAlert;
        this.lowBalanceAlert = lowBalanceAlert;
    }

    public boolean isTransactionAlert() { return transactionAlert; }
    public boolean isRequestAlert() { return requestAlert; }
    public boolean isLowBalanceAlert() { return lowBalanceAlert; }
}