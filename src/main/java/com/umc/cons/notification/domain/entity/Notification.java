package com.umc.cons.notification.domain.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.umc.cons.common.util.BaseTimeEntity;
import com.umc.cons.content.domain.entity.Content;
import com.umc.cons.member.domain.entity.Member;
import com.umc.cons.notification.dto.NotificationRequestDto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member member;

	@ManyToOne
	@JoinColumn(name = "content")
	private Content content;

	private String email;

	private LocalDateTime time;

	@Column(name = "is_deleted")
	private boolean isDeleted;

	private String uuid;

	@Builder
	public Notification(Member member, String email, Content content, LocalDateTime time, String uuid) {
		this.member = member;
		this.email = email;
		this.content = content;
		this.time = time;
		this.uuid = uuid;
		this.isDeleted = false;
	}

	public void deleteNotification() {
		this.isDeleted = true;
	}

	public void updateNotification(NotificationRequestDto requestDto, Content content) {
		this.email = requestDto.getEmail();
		this.content = content;
		this.time = requestDto.getTime();
	}
}
