package com.umc.cons.notification.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.umc.cons.content.domain.entity.Content;
import com.umc.cons.member.domain.entity.Member;
import com.umc.cons.notification.domain.entity.Notification;
import com.umc.cons.notification.domain.repository.NotificationRepository;
import com.umc.cons.notification.dto.NotificationRequestDto;
import com.umc.cons.notification.dto.NotificationResponseDto;
import com.umc.cons.notification.exception.NotificationNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

	private final NotificationRepository notificationRepository;

	public void registerNotification(Notification notification) {
		notificationRepository.save(notification);
	}

	@Transactional(readOnly = true)
	public List<NotificationResponseDto> getNotifications(Member member) {
		List<NotificationResponseDto> notifications =
			notificationRepository.findAllByMember(member)
				.stream().map(NotificationResponseDto::of).collect(Collectors.toList());

		return notifications;
	}

	@Transactional
	public void deleteNotification(String uuid) {
		Notification notification = notificationRepository.findByUuid(uuid)
			.orElseThrow(NotificationNotFoundException::new);

		notification.deleteNotification();
		notificationRepository.save(notification);
	}

	@Transactional
	public Notification updateNotification(NotificationRequestDto requestDto, Content content) {
		Notification notification = notificationRepository.findByUuid(requestDto.getUuid())
			.orElseThrow(NotificationNotFoundException::new);

		notification.updateNotification(requestDto, content);
		notificationRepository.save(notification);

		return notification;
	}

	@Transactional(readOnly = true)
	public List<Notification> findNotificationsByTime(LocalDateTime time) {
		return notificationRepository.findAllByTime(time);
	}

}
