package com.revpay.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.revpay.entity.NotificationPreference;
import com.revpay.entity.User;

public interface NotificationPreferenceRepository  extends JpaRepository<NotificationPreference, Long>{

	Optional<NotificationPreference> findByUser(User user);
	
}
