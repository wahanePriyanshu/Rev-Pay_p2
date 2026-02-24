package com.revpay.controller;

import com.revpay.dto.NotificationResponse;
import com.revpay.service.NotificationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public List<NotificationResponse> myNotifications() {
        return notificationService.getMyNotifications();
    }

    @PostMapping("/{id}/read")
    public String markRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return "Notification marked as read";
    }
}