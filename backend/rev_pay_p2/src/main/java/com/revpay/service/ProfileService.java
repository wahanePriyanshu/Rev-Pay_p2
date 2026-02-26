package com.revpay.service;

import com.revpay.dto.ChangePasswordRequest;
import com.revpay.dto.ProfileResponse;
import com.revpay.dto.UpdateProfileRequest;

public interface ProfileService {

	void updateProfile(UpdateProfileRequest request);
	void changePassword(ChangePasswordRequest request);

	ProfileResponse getProfile();
	 void setTransactionPin(String pin);
	 void changeTransactionPin(String oldPin, String newPin);
	
}
