package com.revpay.service;

import java.util.List;

import com.revpay.dto.NotificationResponse;

public interface NotificationService {


	List<NotificationResponse> getMyNotifications();

    void markAsRead(Long notificationId);

    void createNotification(Long userId, String type, String title, String message);
}
