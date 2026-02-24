package com.revpay.dto;

import java.math.BigDecimal;

public class WalletResponse {
    private Long walletId;
    private BigDecimal balance;
    private String pin;
    
    
    

    public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public void setWalletId(Long walletId) {
		this.walletId = walletId;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public WalletResponse(Long walletId, BigDecimal balance) {
        this.walletId = walletId;
        this.balance = balance;
    }

    public Long getWalletId()
    { return walletId;
    }
    public BigDecimal getBalance(){
    	
    	return balance; 
    	}
}