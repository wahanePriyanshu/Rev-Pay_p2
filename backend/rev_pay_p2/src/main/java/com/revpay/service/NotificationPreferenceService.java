package com.revpay.service;

import com.revpay.dto.NotificationPreferenceResponse;
import com.revpay.dto.UpdateNotificationPreferenceRequest;

public interface NotificationPreferenceService {

    NotificationPreferenceResponse getMyPreferences();

    NotificationPreferenceResponse updateMyPreferences(UpdateNotificationPreferenceRequest request);
}