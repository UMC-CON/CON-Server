package com.umc.cons.member.controller;

import com.umc.cons.common.config.BaseResponse;
import com.umc.cons.common.config.BaseResponseStatus;
import com.umc.cons.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/duplicated-email/{email}")
    private BaseResponse<BaseResponseStatus> isDuplicatedEmail(@PathVariable String email) {
        boolean isDuplicated = memberService.isEmailDuplicated(email);

        if (isDuplicated) {
            return new BaseResponse(BaseResponseStatus.RESPONSE_CONFLICT);
        }
        return new BaseResponse(BaseResponseStatus.SUCCESS);
    }

    @GetMapping("/duplicated-name/{name}")
    private BaseResponse<BaseResponseStatus> isDuplicatedNickname(@PathVariable String name) {
        boolean isDuplicated = memberService.isNameDuplicated(name);

        if (isDuplicated) {
            return new BaseResponse(BaseResponseStatus.RESPONSE_CONFLICT);
        }
        return new BaseResponse(BaseResponseStatus.SUCCESS);
    }

}
