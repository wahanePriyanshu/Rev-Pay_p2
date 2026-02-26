package com.revpay.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revpay.dto.ChangePasswordRequest;
import com.revpay.dto.ChangePinRequest;
import com.revpay.dto.SetPinRequest;
import com.revpay.dto.UpdateProfileRequest;
import com.revpay.dto.UserResponse;
import com.revpay.entity.Role;
import com.revpay.entity.User;
import com.revpay.repository.UserRepository;
import com.revpay.service.ProfileService;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

	private final UserRepository userRepository;
    private final ProfileService profileService;

    public ProfileController(ProfileService profileService,UserRepository userRepository) {
        this.profileService = profileService;
        this.userRepository=userRepository;
    }

    @PutMapping
    public String updateProfile(@RequestBody UpdateProfileRequest request) {
        profileService.updateProfile(request);
        return "Profile updated successfully";
    }
    
    @PostMapping("/change-password")
    public String changePassword(@RequestBody ChangePasswordRequest request) {
        profileService.changePassword(request);
        return "Password changed successfully";
    }
    
    @GetMapping
    public UserResponse getProfile(Authentication authentication) {

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserResponse dto = new UserResponse();
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());

        dto.setRole(
                user.getRoles()
                        .stream()
                        .map(Role::getName)
                        .findFirst()
                        .orElse(null)
        );

        return dto;
    }
    
    
    
    @PostMapping("/set-pin")
    public String setPin(@RequestBody SetPinRequest request) {
        profileService.setTransactionPin(request.getPin());
        return "Transaction PIN set successfully";
    }

    @PostMapping("/change-pin")
    public String changePin(@RequestBody ChangePinRequest request) {
        profileService.changeTransactionPin(request.getOldPin(), request.getNewPin());
        return "Transaction PIN changed successfully";
    }
    
    
    
    
    
}