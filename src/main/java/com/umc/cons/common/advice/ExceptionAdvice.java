package com.umc.cons.common.advice;

import com.umc.cons.common.config.BaseResponse;
import com.umc.cons.common.config.BaseResponseStatus;
import com.umc.cons.common.jwt.exception.InvalidJwtException;
import com.umc.cons.common.refreshtoken.exception.RefreshTokenNotFoundException;
import com.umc.cons.member.exception.MemberNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(MemberNotFoundException.class)
    BaseResponse<BaseResponseStatus> memberNotFoundException() {
        return new BaseResponse(BaseResponseStatus.RESPONSE_MEMBER_NOT_FOUND);
    }

    @ExceptionHandler(InvalidJwtException.class)
    BaseResponse<BaseResponseStatus> invalidJwtException() {
        return new BaseResponse(BaseResponseStatus.INVALID_JWT);
    }

    @ExceptionHandler(RefreshTokenNotFoundException.class)
    BaseResponse<BaseResponseStatus> refreshTokenNotFoundException() {
        return new BaseResponse(BaseResponseStatus.INVALID_JWT);
    }
}
