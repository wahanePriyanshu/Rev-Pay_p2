package com.revpay.service.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.revpay.dto.ChangePasswordRequest;
import com.revpay.dto.UpdateProfileRequest;
import com.revpay.entity.User;
import com.revpay.repository.UserRepository;
import com.revpay.service.ProfileService;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public ProfileServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        return userRepository.findByEmail(username)
                .or(() -> userRepository.findByPhone(username))
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public void updateProfile(UpdateProfileRequest request) {
        User user = getCurrentUser();

        // Update full name
        if (request.getFullName() != null && !request.getFullName().isBlank()) {
            user.setFullName(request.getFullName());
        }

        // Update phone (check uniqueness)
        if (request.getPhone() != null && !request.getPhone().isBlank()) {
            if (!request.getPhone().equals(user.getPhone())) {
                userRepository.findByPhone(request.getPhone())
                        .ifPresent(u -> {
                            throw new RuntimeException("Phone already in use");
                        });
                user.setPhone(request.getPhone());
            }
        }

        userRepository.save(user);
    }

    @Override
    public void changePassword(ChangePasswordRequest request) {
        User user = getCurrentUser();

        if (request.getOldPassword() == null || request.getNewPassword() == null) {
            throw new RuntimeException("Old password and new password are required");
        }

        // 1. Check old password
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Old password is incorrect");
        }

        // 2. Validate new password strength
        String newPassword = request.getNewPassword();
        if (newPassword.length() < 8) {
            throw new RuntimeException("New password must be at least 8 characters long");
        }

        boolean hasUpper = newPassword.chars().anyMatch(Character::isUpperCase);
        boolean hasLower = newPassword.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = newPassword.chars().anyMatch(Character::isDigit);

        if (!hasUpper || !hasLower || !hasDigit) {
            throw new RuntimeException("Password must contain upper, lower case letters and a digit");
        }

        // 3. Hash and save
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

	@Override
	public void setTransactionPin(String pin) {
		
		
		User user = getCurrentUser();

        if (user.getTransactionPin() !=null) {
            throw new RuntimeException("Transaction PIN already set. Use change PIN.");
        }

        validatePin(pin);

        String hashedPin = passwordEncoder.encode(pin);
        user.setTransactionPin(hashedPin);
        userRepository.save(user);
		
	}

	@Override
	public void changeTransactionPin(String oldPin, String newPin) {
		User user = getCurrentUser();

        if (user.getTransactionPin() == null) {
            throw new RuntimeException("Transaction PIN not set yet");
        }

        if (!passwordEncoder.matches(oldPin, user.getTransactionPin())) {
            throw new RuntimeException("Old PIN is incorrect");
        }

        validatePin(newPin);

        String hashedNewPin = passwordEncoder.encode(newPin);
        user.setTransactionPin(hashedNewPin);
        userRepository.save(user);
		
	}
	
	// ---------- VALIDATION ----------
    private void validatePin(String pin) {
        if (pin == null || !pin.matches("\\d{4,6}")) {
            throw new RuntimeException("PIN must be 4 to 6 digits");
        }
    }
		
	
	
}