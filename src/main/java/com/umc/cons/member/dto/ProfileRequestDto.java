package com.umc.cons.member.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProfileRequestDto {
	private MultipartFile imageFile;
	private String name;
	private String introduction;
}
