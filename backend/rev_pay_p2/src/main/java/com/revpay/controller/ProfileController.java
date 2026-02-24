package com.revpay.controller;

import org.springframework.web.bind.annotation.*;

import com.revpay.dto.ChangePasswordRequest;
import com.revpay.dto.ChangePinRequest;
import com.revpay.dto.SetPinRequest;
import com.revpay.dto.UpdateProfileRequest;
import com.revpay.service.ProfileService;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
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