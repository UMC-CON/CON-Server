package com.umc.cons.member.controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.umc.cons.common.annotation.LoginMember;
import com.umc.cons.common.config.BaseResponse;
import com.umc.cons.common.config.BaseResponseStatus;
import com.umc.cons.member.domain.entity.Member;
import com.umc.cons.member.dto.MemberDto;
import com.umc.cons.member.dto.MemberPageResponse;
import com.umc.cons.member.dto.OAuth2MemberDto;
import com.umc.cons.member.dto.PasswordRequestDto;
import com.umc.cons.member.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController()
@RequestMapping("/members")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

	private final MemberService memberService;
	private final PasswordEncoder passwordEncoder;

	@PostMapping("/sign-up")
	public BaseResponse<BaseResponseStatus> registration(@RequestBody @Valid MemberDto memberDto) {
		boolean isDuplicatedEmail = memberService.isDuplicatedEmail(memberDto.getEmail());
		boolean isDuplicatedName = memberService.isDuplicatedName(memberDto.getName());
		boolean checkPassword = memberService.checkPassword(memberDto.getPassword(), memberDto.getCheckPassword());

		if (isDuplicatedEmail) {
			return new BaseResponse(BaseResponseStatus.REQUEST_DUPLICATED_EMAIL);
		}

		if (isDuplicatedName) {
			return new BaseResponse(BaseResponseStatus.REQUEST_DUPLICATED_NAME);
		}

		if (checkPassword) {
			Member member = MemberDto.toEntity(memberDto, passwordEncoder);
			memberService.registerMember(member);

			return new BaseResponse(BaseResponseStatus.SUCCESS);
		}

		return new BaseResponse(BaseResponseStatus.REQUEST_CHECK_PASSWORD);

	}

	@PostMapping("/oauth2/sign-up")
	public BaseResponse<BaseResponseStatus> registration(@RequestBody OAuth2MemberDto oAuth2MemberDto,
		@LoginMember Member member) {
		boolean isDuplicatedName = memberService.isDuplicatedName(oAuth2MemberDto.getName());

		if (isDuplicatedName) {
			return new BaseResponse(BaseResponseStatus.REQUEST_DUPLICATED_NAME);
		}

		memberService.registerOAuth2Member(member, oAuth2MemberDto.getName());

		return new BaseResponse(BaseResponseStatus.SUCCESS);
	}

	@GetMapping()
	public BaseResponse<MemberPageResponse> getMembersByName(@RequestParam("name") @NotEmpty String name,
		Pageable pageable) {
		MemberPageResponse page = memberService.findAllByName(name, pageable);

		return new BaseResponse(page);
	}

	@PostMapping("/password")
	public BaseResponse<BaseResponseStatus> updatePassword(@RequestBody @Valid PasswordRequestDto passwordRequestDto,
		@LoginMember Member member) {
		boolean checkMemberPassword = passwordEncoder.matches(passwordRequestDto.getCurrentPassword(),
			member.getPassword());

		if (checkMemberPassword) {
			memberService.updatePassword(member, passwordEncoder, passwordRequestDto.getPassword());

			return new BaseResponse<>(BaseResponseStatus.SUCCESS);
		}

		return new BaseResponse<>(BaseResponseStatus.REQUEST_CHECK_PASSWORD);
	}

	@DeleteMapping()
	public BaseResponse<BaseResponseStatus> deleteMember(@LoginMember Member member, HttpServletResponse response) {
		memberService.deleteMember(member);

		return new BaseResponse<>(BaseResponseStatus.SUCCESS);
	}

}
