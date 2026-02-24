package com.revpay.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "notification_preferences")
public class NotificationPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "transaction_alert", nullable = false)
    private Boolean transactionAlert = true;

    @Column(name = "request_alert", nullable = false)
    private Boolean requestAlert = true;

    @Column(name = "low_balance_alert", nullable = false)
    private Boolean lowBalanceAlert = true;

    // Getters & Setters
    public Long getId() { return id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Boolean getTransactionAlert() { return transactionAlert; }
    public void setTransactionAlert(Boolean transactionAlert) { this.transactionAlert = transactionAlert; }

    public Boolean getRequestAlert() { return requestAlert; }
    public void setRequestAlert(Boolean requestAlert) { this.requestAlert = requestAlert; }

    public Boolean getLowBalanceAlert() { return lowBalanceAlert; }
    public void setLowBalanceAlert(Boolean lowBalanceAlert) { this.lowBalanceAlert = lowBalanceAlert; }
}