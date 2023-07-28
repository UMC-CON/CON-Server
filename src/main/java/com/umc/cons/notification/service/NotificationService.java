package com.umc.cons.notification.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.umc.cons.member.domain.entity.Member;
import com.umc.cons.notification.domain.entity.Notification;
import com.umc.cons.notification.domain.repository.NotificationRepository;
import com.umc.cons.notification.dto.NotificationResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

	private final NotificationRepository notificationRepository;

	public void registerNotification(Notification notification) {
		notificationRepository.save(notification);
	}

	public List<NotificationResponseDto> getNotifications(Member member) {
		List<NotificationResponseDto> notifications =
			notificationRepository.findAllByMember(member)
				.stream().map(NotificationResponseDto::of).collect(Collectors.toList());

		return notifications;
	}

}
