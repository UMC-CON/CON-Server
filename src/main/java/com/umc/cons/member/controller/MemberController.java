package com.umc.cons.member.controller;

import com.umc.cons.common.annotation.LoginMember;
import com.umc.cons.common.config.BaseResponse;
import com.umc.cons.common.config.BaseResponseStatus;
import com.umc.cons.common.jwt.service.JwtService;
import com.umc.cons.member.domain.entity.Member;
import com.umc.cons.member.dto.MemberDto;
import com.umc.cons.member.dto.OAuth2MemberDto;
import com.umc.cons.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController()
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/sign-up")
    public BaseResponse<BaseResponseStatus> registration(@RequestBody @Valid MemberDto memberDto) {
        boolean isDuplicatedEmail = memberService.isDuplicatedEmail(memberDto.getEmail());
        boolean isDuplicatedName = memberService.isDuplicatedName(memberDto.getName());
        boolean checkPassword = memberService.checkPassword(memberDto.getPassword(), memberDto.getCheckPassword());

        if (isDuplicatedEmail) {
            return new BaseResponse(BaseResponseStatus.RESPONSE_DUPLICATED_EMAIL);
        }

        if (isDuplicatedName) {
            return new BaseResponse(BaseResponseStatus.RESPONSE_DUPLICATED_NAME);
        }

        if (checkPassword) {
            Member member = MemberDto.toEntity(memberDto,passwordEncoder);
            memberService.registerMember(member);

            return new BaseResponse(BaseResponseStatus.SUCCESS);
        }

        return new BaseResponse(BaseResponseStatus.RESPONSE_CHECK_PASSWORD);

    }

    @PostMapping("/oauth2/sign-up")
    private BaseResponse<BaseResponseStatus> registration(@RequestBody OAuth2MemberDto oAuth2MemberDto, @LoginMember Member member) {
        boolean isDuplicatedName = memberService.isDuplicatedName(oAuth2MemberDto.getName());

        if(isDuplicatedName) {
            return new BaseResponse(BaseResponseStatus.RESPONSE_DUPLICATED_NAME);
        }

        memberService.registerOAuth2Member(member, oAuth2MemberDto.getName());

        return new BaseResponse(BaseResponseStatus.SUCCESS);
    }

}
