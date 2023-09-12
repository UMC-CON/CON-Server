package com.umc.cons.member.dto;

import com.umc.cons.member.domain.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponse {
	private Long id;
	private String name;
	private String imageUrl;
	private String introduction;

	public static MemberResponse of(Member member) {
		return MemberResponse.builder()
			.id(member.getId())
			.name(member.getName())
			.imageUrl(member.getImageUrl())
			.introduction(member.getIntroduction())
			.build();
	}
}
