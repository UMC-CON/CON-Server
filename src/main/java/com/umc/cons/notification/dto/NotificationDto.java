package com.umc.cons.notification.dto;

import java.time.LocalDateTime;

import com.umc.cons.member.domain.entity.Member;
import com.umc.cons.notification.domain.entity.Notification;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NotificationDto {

	private String title;

	private LocalDateTime time;

	private String email;

	public static Notification toEntity(NotificationDto notificationDto, Member member) {
		return Notification.builder()
			.email(notificationDto.email)
			.title(notificationDto.title)
			.time(notificationDto.time)
			.member(member)
			.build();
	}
}
