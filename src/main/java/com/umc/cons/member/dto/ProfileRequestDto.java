package com.umc.cons.member.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProfileRequestDto {
	private String imageUrl;
	private String name;
	private String introduction;
}
