package com.umc.cons.notification.service;

import org.springframework.stereotype.Service;

import com.umc.cons.notification.domain.entity.Notification;
import com.umc.cons.notification.domain.repository.NotificationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

	private final NotificationRepository notificationRepository;

	public void registerNotification(Notification notification) {
		notificationRepository.save(notification);
	}

}
