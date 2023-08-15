package com.umc.cons.notification.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.umc.cons.content.domain.entity.Content;
import com.umc.cons.member.domain.entity.Member;
import com.umc.cons.notification.domain.entity.Notification;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
@Getter
public class NotificationDto {

	private Long contentId;

	private LocalDateTime time;

	private String email;

	public static Notification toEntity(NotificationDto notificationDto, Member member, Content content) {
		return Notification.builder()
			.email(notificationDto.email)
			.content(content)
			.time(notificationDto.time)
			.member(member)
			.uuid(UUID.randomUUID().toString())
			.build();
	}
}
