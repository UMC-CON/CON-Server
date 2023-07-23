package com.umc.cons.member.controller;

import com.umc.cons.common.config.BaseResponse;
import com.umc.cons.common.config.BaseResponseStatus;
import com.umc.cons.member.domain.entity.Member;
import com.umc.cons.member.dto.MemberDto;
import com.umc.cons.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/duplicated-email/{email}")
    private BaseResponse<BaseResponseStatus> isDuplicatedEmail(@PathVariable String email) {
        boolean isDuplicated = memberService.isDuplicatedEmail(email);

        if (isDuplicated) {
            return new BaseResponse(BaseResponseStatus.RESPONSE_CONFLICT);
        }
        return new BaseResponse(BaseResponseStatus.SUCCESS);
    }

    @GetMapping("/duplicated-name/{name}")
    private BaseResponse<BaseResponseStatus> isDuplicatedName(@PathVariable String name) {
        boolean isDuplicated = memberService.isDuplicatedName(name);

        if (isDuplicated) {
            return new BaseResponse(BaseResponseStatus.RESPONSE_CONFLICT);
        }
        return new BaseResponse(BaseResponseStatus.SUCCESS);
    }

    @PostMapping("/sign-up")
    public BaseResponse<BaseResponseStatus> registration(@RequestBody @Valid MemberDto memberDto) {
        boolean isDuplicatedEmail = memberService.isDuplicatedEmail(memberDto.getEmail());
        boolean isDuplicatedName = memberService.isDuplicatedName(memberDto.getName());
        boolean checkPassword = memberService.checkPassword(memberDto.getPassword(), memberDto.getCheckPassword());

        if (isDuplicatedEmail || isDuplicatedName) {
            return new BaseResponse(BaseResponseStatus.RESPONSE_CONFLICT);
        }

        if (checkPassword) {
            Member member = MemberDto.toEntity(memberDto,passwordEncoder);
            memberService.registerMember(member);

            return new BaseResponse(BaseResponseStatus.SUCCESS);
        }

        return new BaseResponse(BaseResponseStatus.RESPONSE_CONFLICT);

    }

}
