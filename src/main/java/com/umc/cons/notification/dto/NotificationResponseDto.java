package com.umc.cons.notification.dto;

import java.time.LocalDateTime;

import com.umc.cons.notification.domain.entity.Notification;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class NotificationResponseDto {

	private String contentName;

	private String email;

	private LocalDateTime time;

	private String uuid;

	public static NotificationResponseDto of(Notification notification) {
		return NotificationResponseDto.builder()
			.contentName(notification.getContent().getName())
			.email(notification.getEmail())
			.time(notification.getTime())
			.uuid(notification.getUuid())
			.build();
	}
}
