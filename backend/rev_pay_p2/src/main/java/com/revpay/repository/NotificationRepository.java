package com.revpay.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.revpay.entity.Notification;
import com.revpay.entity.User;

public interface NotificationRepository extends JpaRepository<Notification, Long>{

	List<Notification> findByUserOrderByCreatedAtDesc(User user);
}
