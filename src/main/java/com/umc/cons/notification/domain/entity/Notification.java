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
import com.umc.cons.member.domain.entity.Member;

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

	private String email;

	private String title;

	private LocalDateTime time;

	@Column(name = "is_deleted", columnDefinition = "boolean default false")
	private boolean isDeleted = false;

	private String uuid;

	@Builder
	public Notification(Member member, String email, String title, LocalDateTime time, String uuid) {
		this.member = member;
		this.email = email;
		this.title = title;
		this.time = time;
		this.uuid = uuid;
	}
}
