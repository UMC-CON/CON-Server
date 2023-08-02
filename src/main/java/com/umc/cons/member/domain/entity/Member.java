package com.umc.cons.member.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.umc.cons.common.util.BaseTimeEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, name = "email")
	private String email;

	private String password;

	@Column(name = "image_url")
	private String imageUrl;

	@Column(name = "name")
	private String name;

	@Column(name = "is_deleted")
	private boolean isDeleted;

	private String introduction;

	@Enumerated(EnumType.STRING)
	private Role role;

	@Enumerated(EnumType.STRING)
	private SocialType socialType;

	@Column(name = "social_id")
	private String socialId; // 로그인한 소셜 타입의 식별자 값 (일반 로그인시 null)

	@Builder
	public Member(String email, String password, String imageUrl, String name,
		SocialType socialType, String socialId, Role role) {
		this.email = email;
		this.password = password;
		this.imageUrl = imageUrl;
		this.name = name;
		this.socialType = socialType;
		this.socialId = socialId;
		this.role = role;
		this.isDeleted = false;
		this.introduction = "자기소개를 입력하세요!";
	}

	public void registerOAuth2User(String name) {
		this.name = name;
		this.role = Role.USER;
	}

	public void updatePassword(String password) {
		this.password = password;
	}

	public void deleteMember() {
		this.isDeleted = true;
	}

}
