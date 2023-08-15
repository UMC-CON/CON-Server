package com.umc.cons.notification.dto;

import java.time.LocalDateTime;

import com.umc.cons.content.domain.entity.Content;
import com.umc.cons.notification.domain.entity.Notification;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class NotificationRequestDto {

	private String uuid;
	private Long contentId;
	private String email;
	private LocalDateTime time;

	public static Notification toEntity(NotificationRequestDto notificationRequestDto, Content content) {
		return Notification.builder()
			.uuid(notificationRequestDto.getUuid())
			.content(content)
			.email(notificationRequestDto.getEmail())
			.time(notificationRequestDto.getTime())
			.build();
	}
}
