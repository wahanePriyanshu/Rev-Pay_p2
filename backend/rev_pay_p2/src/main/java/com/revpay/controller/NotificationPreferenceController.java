package com.revpay.controller;

import com.revpay.dto.NotificationPreferenceResponse;
import com.revpay.dto.UpdateNotificationPreferenceRequest;
import com.revpay.service.NotificationPreferenceService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notification-preferences")
public class NotificationPreferenceController {

    private final NotificationPreferenceService preferenceService;

    public NotificationPreferenceController(NotificationPreferenceService preferenceService) {
        this.preferenceService = preferenceService;
    }

    // Get my preferences
    @GetMapping
    public NotificationPreferenceResponse getMyPreferences() {
        return preferenceService.getMyPreferences();
    }

    //  Update my preferences
    @PutMapping
    public NotificationPreferenceResponse updateMyPreferences(
            @RequestBody UpdateNotificationPreferenceRequest request) {
        return preferenceService.updateMyPreferences(request);
    }
}