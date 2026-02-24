package com.revpay.dto;

import java.time.LocalDateTime;

public class NotificationResponse {

	private Long id;
	private String title;
	private String message;
	private Boolean isRead;
	private LocalDateTime createdAt;
	public NotificationResponse(Long id, String title, String message, Boolean isRead, LocalDateTime createdAt) {
		super();
		this.id = id;
		this.title = title;
		this.message = message;
		this.isRead = isRead;
		this.createdAt = createdAt;
	}
	public Long getId() {
		return id;
	}
	public String getTitle() {
		return title;
	}
	public String getMessage() {
		return message;
	}
	public Boolean getIsRead() {
		return isRead;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	
	
}
