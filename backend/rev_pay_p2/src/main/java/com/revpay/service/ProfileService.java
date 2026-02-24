package com.revpay.service;

import com.revpay.dto.ChangePasswordRequest;
import com.revpay.dto.UpdateProfileRequest;

public interface ProfileService {

	void updateProfile(UpdateProfileRequest request);
	void changePassword(ChangePasswordRequest request);


	 void setTransactionPin(String pin);
	 void changeTransactionPin(String oldPin, String newPin);
	
}
