package com.revpay.service.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.revpay.dto.NotificationPreferenceResponse;
import com.revpay.dto.UpdateNotificationPreferenceRequest;
import com.revpay.entity.NotificationPreference;
import com.revpay.entity.User;
import com.revpay.repository.NotificationPreferenceRepository;
import com.revpay.repository.UserRepository;
import com.revpay.service.NotificationPreferenceService;

@Service
public class NotificationPreferenceServiceImpl implements NotificationPreferenceService {

    private final NotificationPreferenceRepository preferenceRepository;
    private final UserRepository userRepository;

    public NotificationPreferenceServiceImpl(NotificationPreferenceRepository preferenceRepository,
                                             UserRepository userRepository) {
        this.preferenceRepository = preferenceRepository;
        this.userRepository = userRepository;
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        return userRepository.findByEmail(username)
                .or(() -> userRepository.findByPhone(username))
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private NotificationPreference getOrCreate(User user) {
        return preferenceRepository.findByUser(user)
                .orElseGet(() -> {
                    NotificationPreference p = new NotificationPreference();
                    p.setUser(user);
                    p.setTransactionAlert(true);
                    p.setRequestAlert(true);
                    p.setLowBalanceAlert(true);
                    return preferenceRepository.save(p);
                });
    }

    @Override
    public NotificationPreferenceResponse getMyPreferences() {
        User user = getCurrentUser();
        NotificationPreference pref = getOrCreate(user);

        return new NotificationPreferenceResponse(
                pref.getTransactionAlert(),
                pref.getRequestAlert(),
                pref.getLowBalanceAlert()
        );
    }

    @Override
    public NotificationPreferenceResponse updateMyPreferences(UpdateNotificationPreferenceRequest request) {
        User user = getCurrentUser();
        NotificationPreference pref = getOrCreate(user);

        pref.setTransactionAlert(request.isTransactionAlert());
        pref.setRequestAlert(request.isRequestAlert());
        pref.setLowBalanceAlert(request.isLowBalanceAlert());

        preferenceRepository.save(pref);

        return new NotificationPreferenceResponse(
                pref.getTransactionAlert(),
                pref.getRequestAlert(),
                pref.getLowBalanceAlert()
        );
    }
}