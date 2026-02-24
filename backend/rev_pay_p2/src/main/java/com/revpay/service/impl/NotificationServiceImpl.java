package com.revpay.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.revpay.dto.NotificationResponse;
import com.revpay.entity.Notification;
import com.revpay.entity.NotificationPreference;
import com.revpay.entity.User;
import com.revpay.repository.NotificationPreferenceRepository;
import com.revpay.repository.NotificationRepository;
import com.revpay.repository.UserRepository;
import com.revpay.service.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final NotificationPreferenceRepository preferenceRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository,
                                   UserRepository userRepository,
                                   NotificationPreferenceRepository preferenceRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.preferenceRepository = preferenceRepository;
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        return userRepository.findByEmail(username)
                .or(() -> userRepository.findByPhone(username))
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public List<NotificationResponse> getMyNotifications() {
        User user = getCurrentUser();

        return notificationRepository.findByUserOrderByCreatedAtDesc(user)
                .stream()
                .map(n -> new NotificationResponse(
                        n.getId(),
                        n.getTitle(),
                        n.getMessage(),
                        n.getIsRead(),
                        n.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public void markAsRead(Long notificationId) {
        User user = getCurrentUser();

        Notification n = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        if (!n.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Not allowed");
        }

        n.setIsRead(true);
        notificationRepository.save(n);
    }

    @Override
    public void createNotification(Long userId, String type, String title, String message) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 🔐 Check user preference
        if (!isNotificationAllowed(user, type)) {
            return; // skip notification silently
        }

        Notification n = new Notification();
        n.setUser(user);
        n.setTitle(title);
        n.setMessage(message);
        n.setIsRead(false);
        n.setCreatedAt(LocalDateTime.now());

        notificationRepository.save(n);
    }

    private boolean isNotificationAllowed(User user, String type) {
        NotificationPreference pref = preferenceRepository.findByUser(user)
                .orElseGet(() -> {
                    // Create default preferences if not exist
                    NotificationPreference p = new NotificationPreference();
                    p.setUser(user);
                    p.setTransactionAlert(true);
                    p.setRequestAlert(true);
                    p.setLowBalanceAlert(true);
                    return preferenceRepository.save(p);
                });

        return switch (type) {
            case "TRANSACTION" -> pref.getTransactionAlert();
            case "REQUEST" -> pref.getRequestAlert();
            case "LOW_BALANCE" -> pref.getLowBalanceAlert();
            default -> true;
        };
    }
}